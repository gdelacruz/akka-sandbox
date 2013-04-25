package akka.first.app.scala.actors

import scala.collection.immutable.Map

import akka.actor.Actor
import akka.actor.actorRef2Scala
import akka.first.app.scala.MapData
import akka.first.app.scala.ReduceData
import akka.first.app.scala.WordCount

class ReduceActor extends Actor {

  val defaultCount: Int = 1
  def receive: Receive = {
    case MapData(dataList) =>
      sender ! reduce(dataList)
  }

  def reduce(words: IndexedSeq[WordCount]): ReduceData = ReduceData {
    words.foldLeft(Map.empty[String, Int]) { (index, words) =>
      if (index contains words.word)
        index + (words.word -> (index.get(words.word).get + 1))
      else
        index + (words.word -> 1)
    }
  }

}