package ru.train.controller

import cats.effect.IO

trait WebCrawlerProxyControllerAlgebra[F[_]] {

  def getTilesByUrlList(list: List[String]): List[(String, String)]
}
