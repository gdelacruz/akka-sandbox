package org.akka.essentials.wc.mapreduce.example.server

import scala.collection.immutable.Map
import scala.collection.mutable.HashMap
import akka.actor.Actor
import akka.actor.actorRef2Scala
import akka.actor.ActorLogging

class AggregateActor extends Actor with ActorLogging{

  val finalReducedMap = new HashMap[String, Int]

  def receive = {
    case ReduceData(reduceDataMap) =>
      aggregateInMemoryReduce(reduceDataMap)
    case message: String if (message == "DISPLAY_LIST") =>
      log.info(finalReducedMap.toString)
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