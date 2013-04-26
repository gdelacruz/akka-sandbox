name := "ActorMessagingExample"

version := "1.0"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-feature","-deprecation")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.2"

sources in (Compile, compile) ~= (_ filter (_.getName endsWith ".scala"))