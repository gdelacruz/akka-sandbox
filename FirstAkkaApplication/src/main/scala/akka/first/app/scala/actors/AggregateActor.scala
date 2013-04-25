package akka.first.app.scala.actors

import scala.collection.immutable.Map
import scala.collection.mutable.HashMap

import akka.actor.Actor
import akka.actor.actorRef2Scala
import akka.first.app.scala.ReduceData
import akka.first.app.scala.Result

class AggregateActor extends Actor {

  val finalReducedMap = new HashMap[String, Int]

  def receive: Receive = {
    case ReduceData(reduceDataMap) =>
      aggregateInMemoryReduce(reduceDataMap)
    case message: Result =>
      sender ! finalReducedMap.toString
  }

  def aggregateInMemoryReduce(reducedList: Map[String, Int]): Unit = {
    for ((key, value) <- reducedList) {
      if (finalReducedMap contains key)
        finalReducedMap(key) = (value + finalReducedMap.get(key).get)
      else
        finalReducedMap += (key -> value)
    }
  }

}