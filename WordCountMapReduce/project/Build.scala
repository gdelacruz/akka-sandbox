package org.akka.essentials.wc.mapreduce.example

import sbt._
import Keys._

object WorldCountMapReduce extends Build {
  val Organization = "WordCountMapReduce"
  val Version      = "1.0"
  val ScalaVersion = "2.10.1"
 
  lazy val sample = Project(
    id = "main-sample",
    base = file("."),
    settings = defaultSettings ++ Seq(
      libraryDependencies ++= Dependencies.tracedAkka,
      scalacOptions += "-language:postfixOps",
      javaOptions in run ++= Seq(
        "-javaagent:/home/ger/Downloads/typesafe-console-1.2.0-M5/lib/weaver/aspectjweaver.jar",
        "-Dorg.aspectj.tracing.factory=default",
        "-Djava.library.path=/home/ger/Downloads/typesafe-console-1.2.0-M5/lib/sigar"
      ),
      mainClass in (Compile, run) := Some("org.akka.essentials.wc.mapreduce.example.server.Server"),
      //sources in (Compile, compile) ~= (_ filter (_.getName endsWith ".scala")),
      Keys.fork in run := true
    )
  )
 
  lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := Organization,
    version      := Version,
    scalaVersion := ScalaVersion,
    crossPaths   := false
  )
 
  lazy val defaultSettings = buildSettings ++ Seq(
    resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  )
}
 
object Dependencies {
 
  object V {
    val Akka    = "2.1.4"
    val Atmos   = "1.2.0-M5"
    val Logback = "1.0.7"
  }
 
  val actor = "com.typesafe.akka" %% "akka-actor" % "2.1.2"
  val remote = "com.typesafe.akka" %% "akka-remote" % "2.1.2"
  val kernel = "com.typesafe.akka" %% "akka-kernel" % "2.1.2"

  val atmosTrace = "com.typesafe.atmos" % ("trace-akka-" + V.Akka) % V.Atmos
  val logback    = "ch.qos.logback"     % "logback-classic"        % V.Logback
 
  val tracedAkka = Seq(actor, remote, kernel, atmosTrace, logback)
}