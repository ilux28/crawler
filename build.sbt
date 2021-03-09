import Dependencies._
name := "crawler"

version := "0.1"

scalaVersion := "2.13.5"

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-deprecation",
  "-Ywarn-value-discard")

libraryDependencies ++= Seq(cats, http4s, jackson, circe, tests, slf4j).flatten