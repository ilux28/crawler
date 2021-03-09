package ru.train

import cats.MonadError
import cats.data.Kleisli
import cats.effect.{IO, IOApp, _}
import cats.instances.list._
import cats.syntax.parallel._
import io.circe.generic.auto._
import org.http4s
import org.http4s.{EntityDecoder, HttpRoutes, Request}
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.slf4j.{Logger, LoggerFactory}
import ru.train.controller.WebCrawlerClient
import sttp.client.Response

import scala.concurrent.ExecutionContext
import scala.util.matching.Regex

/** *
 * EndPoint for service which receive json with urls and send title html
 * @author Ilya Pribytkov
 */

object Main extends IOApp {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  case class Urls(urls: List[String])
  val logger: Logger = LoggerFactory.getLogger("Main Web Crawler")
  val monadError: MonadError[IO, Throwable] = MonadError[IO, Throwable]
  val webCrawlerClient: WebCrawlerClient[IO] = new WebCrawlerClient[IO](monadError)
  implicit val decoder: EntityDecoder[IO, Urls] = jsonOf[IO, Urls]

  val pattern: Regex = """<title>[\s\S]*?</title>""".r
  val Title: Regex = """<title>([\s\S]*?)</title>""".r

  def sequence[A](l: List[Option[A]]): Option[List[A]] = l match {
    case Nil => Some(Nil)
    case h :: t => h match {
      case None => None
      case Some(head) => sequence(t) match {
        case None => None
        case Some(list) => Some(head :: list)
      }
    }
  }

  /***
   * This is route which return List tuples with url and title html page
   * @return
   */

  def crawlerServiceRoutes: Kleisli[IO, Request[IO], http4s.Response[IO]] = HttpRoutes.of[IO] {
    case req@POST -> Root =>
      val resResp = for {
        receiveListUrl <- req.as[Urls]
        receiveListUrls: List[String] = receiveListUrl.urls
        receiveJson: List[IO[Response[String]]] = receiveListUrls.map(webCrawlerClient.getTitle)
        listIoString: List[IO[String]] = receiveJson.map(_.flatMap(elem => IO(elem.body)))
        toResponse: IO[List[String]] = listIoString.parSequence
        htmlBodyList <- toResponse
        titleListOpt: List[Option[String]] = htmlBodyList.map(html => {
          pattern.findFirstIn(html).map(matchValue => {
            val Title(title) = matchValue
            title
          } )
        })
        titleOptList: Option[List[String]] = sequence(titleListOpt)
        resultOpt = titleOptList.map(_.zip(receiveListUrls))
        _ = resultOpt.map((in: Seq[(String, String)]) => in.map(tupleString => logger.info(s"${tupleString._1} ${tupleString._2}")))
        resp <- Ok(resultOpt)
      } yield resp
      resResp
  }.orNotFound

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder.apply[IO](ec)
      .bindHttp(8087, "localhost")
      .withHttpApp(crawlerServiceRoutes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}