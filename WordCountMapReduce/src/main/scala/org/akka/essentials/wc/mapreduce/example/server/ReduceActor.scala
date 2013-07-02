package org.akka.essentials.wc.mapreduce.example.server

import akka.actor.Actor
import akka.actor.ActorRef

class ReduceActor (actor: ActorRef) extends Actor {
  val defaultCount: Int = 1
  def receive: Receive = {
    case MapData(dataList) =>
      actor ! reduce(dataList)
  }

  def reduce(words: List[WordCount]): ReduceData = ReduceData {
    words.foldLeft(Map.empty[String, Int]) { (index, words) =>
      if (index contains words.word)
        index + (words.word -> (index.get(words.word).get + 1))
      else
        index + (words.word -> 1)
    }
  }
}