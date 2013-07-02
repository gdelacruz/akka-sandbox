package org.akka.essentials.wc.mapreduce.example.client

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef

class ClientActor(inRemoteServer: ActorRef) extends Actor with ActorLogging {
  var start: Long = 0

  def receive: Receive = {
    case message: String =>
      // Get reference to the message sender and reply back
      inRemoteServer ! message
  }

  override def preStart = {
    start = System.currentTimeMillis();
  }

  override def postStop = {
    // tell the world that the calculation is complete
    val timeSpent = (System.currentTimeMillis() - start) / 1000;
    log.info("\n\tClientActor estimate: \t\t\n\tCalculation time: \t{} Secs", timeSpent)
  }

}