package ru.train.controller

import cats.effect.{ContextShift, _}
import cats.implicits._
import cats.{FlatMap, MonadError}
import ru.train.exception.CustomException
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.{Response, asStringAlways, basicRequest, _}
import sttp.model.Uri

/***
 * This class is client which send http request by url and return response as html page
 * @return
 */

class WebCrawlerClient[F[_] : Concurrent : ContextShift : FlatMap](M: MonadError[F, Throwable]) {

  private def makeUrl(url: String): Uri = uri"$url"

  def getTitle(uri: String): F[Response[String]] = {
    def resEffect: F[Response[String]] = AsyncHttpClientCatsBackend[F]().flatMap { implicit backend =>
      val response: F[Response[String]] = basicRequest
        .response(asStringAlways)
        .get(makeUrl(uri))
        .send()
      response
    }

    val response: F[Response[String]] = for {
      tempContent <- resEffect
      tempContentOpt = Option(tempContent)
      resContent <- M.fromOption(tempContentOpt, CustomException())
    } yield resContent
    response
  }
}
