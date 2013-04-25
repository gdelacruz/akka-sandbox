package akka.first.app.scala

import scala.collection.immutable.Map
import scala.collection.mutable.ArrayBuffer

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout.durationToTimeout
import akka.first.app.mapreduce.actors.MasterActor
import akka.util.Timeout

sealed trait MapReduceMessage

case class WordCount(val word: String, val count: Integer) extends MapReduceMessage
case class Result extends MapReduceMessage
case class MapData(val dataList: ArrayBuffer[WordCount]) extends MapReduceMessage
case class ReduceData(val reduceDataMap: Map[String, Int]) extends MapReduceMessage

object MapReduceApplication {

  def main(args: Array[String]) {
    val _system = ActorSystem("MapReduceApp")
    val master = _system.actorOf(Props[MasterActor], name = "master")
    implicit val timeout = Timeout(20 seconds)

    master ! "The quick brown fox tried to jump over the lazy dog and fell on the dog"
    master ! "Dog is man's best friend"
    master ! "Dog and Fox belong to the same family"

    Thread.sleep(500)
    val future = (master ? Result()).mapTo[String]

    val result = Await.result(future, timeout.duration)
    
    //master ! Result()

    println(result)

    Thread.sleep(500)
    _system.shutdown
  }
}