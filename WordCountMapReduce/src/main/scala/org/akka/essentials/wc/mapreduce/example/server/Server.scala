package org.akka.essentials.wc.mapreduce.example.server

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props
import akka.kernel.Bootable

class Server extends Bootable {

  val system = ActorSystem.create("WCMapReduceApp", ConfigFactory.load()
    .getConfig("WCMapReduceApp"));

  override def startup = {

    // create the overall WCMapReduce Actor that acts as the remote actor
    // for clients
    val mapReduceActor = system.actorOf(Props[MapReduceActor].withDispatcher("priorityMailBox-dispatcher"), "MapReduceActor");
  }

  def shutdown = {
    system.shutdown
  }
}

