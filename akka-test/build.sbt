name := "akka-test"

version := "1.0.0"

scalaVersion := "2.10.0"

scalacOptions ++= Seq("-deprecation")

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"


libraryDependencies +=
  "com.typesafe.akka" %% "akka-actor" % "2.1.0"
