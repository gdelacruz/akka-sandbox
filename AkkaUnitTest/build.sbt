name := "AkkaUnitTest"

version := "1.0"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-feature","-deprecation")

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.2"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.1.2" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

sources in (Compile, compile) ~= (_ filter (_.getName endsWith ".scala"))

sources in (Test, compile) ~= (_ filter (_.getName endsWith ".scala"))