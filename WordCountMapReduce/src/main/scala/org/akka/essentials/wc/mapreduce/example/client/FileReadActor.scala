package org.akka.essentials.wc.mapreduce.example.client

import java.io.IOException

import scala.io.Source

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.actorRef2Scala

class FileReadActor extends Actor with ActorLogging {

  def receive = {
    case fileName: String =>
      try {
        val reader = Source.fromFile(fileName).getLines

        reader.foreach(sender ! _)

        log.info("All lines send !");

        // send the EOF message..
        sender ! "EOF"
      } catch {
        case x: IOException => System.err.format("IOException: %s%n", x);
      }
    case _ => throw new IllegalArgumentException("Unknown message");
  }

}