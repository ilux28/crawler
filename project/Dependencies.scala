import sbt._

object Dependencies {
  private val tapirVersion = "0.13.0"
  private val scalatestVersion = "3.1.1"
  private val catsVersion = "2.1.1"
  private val http4sVersion = "0.21.4"
  private val slf4jVersion = "1.7.5"
  private val fs2Version = "2.2.2"

  val fs2 = Seq(
    "co.fs2" %% "fs2-core" % fs2Version,
    "co.fs2" %% "fs2-io" % fs2Version,
    "co.fs2" %% "fs2-reactive-streams" % fs2Version,
    "co.fs2" %% "fs2-experimental" % fs2Version
  )

  val tests = Seq(
    "org.scalactic" %% "scalactic" % scalatestVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
  )

  val slf4j = Seq(
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "org.slf4j" % "slf4j-simple" % slf4jVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3" % Runtime
  )

  val cats = Seq("org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsVersion,
    "com.softwaremill.sttp.client" %% "async-http-client-backend-cats" % "2.0.9",
    "com.softwaremill.sttp.client" %% "async-http-client-backend-fs2" % "2.0.9",
    "com.softwaremill.sttp.client" %% "async-http-client-backend-cats" % "2.0.9"
  )

//  val tapir = Seq(
//    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
//    "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
//    "com.softwaremill.sttp.tapir" %% "tapir-server-tests" % tapirVersion % Test,
//    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
//    "com.softwaremill.sttp.client" %% "core" % "2.0.7"
//  )

  val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion % Test,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-core" % http4sVersion,
    "org.http4s" %% "http4s-json4s-native" % http4sVersion ,
    "org.http4s" %% "http4s-json4s-jackson" % http4sVersion ,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "com.softwaremill.sttp.client" %% "http4s-backend" % "2.0.9",
    "org.http4s" %% "http4s-argonaut" % http4sVersion
  )

  val jackson = Seq(
    "com.fasterxml.jackson.core" % "jackson-core" % "2.10.3",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.0.rc1",
    "org.json" % "json" % "20190722",
    "org.json4s" %% "json4s-jackson" % "3.7.0-M2",
    "org.json4s" %% "json4s-native" % "3.6.7",
    "org.http4s" %% "http4s-argonaut" % http4sVersion
  )

  val circe = Seq(
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-parser" % "0.13.0",
    "io.circe" %% "circe-literal" % "0.13.0"
  )

  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
}