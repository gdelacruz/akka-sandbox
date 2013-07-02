package org.akka.essentials.wc.mapreduce.example.client

import com.typesafe.config.ConfigFactory

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.kernel.Bootable

class Client extends Bootable {

  val fileName = "/home/ger/develop/git/akka-sandbox/WordCountMapReduce/src/main/resources/Othello.txt"

  val system = ActorSystem("ClientApplication", ConfigFactory.load().getConfig("WCMapReduceClientApp"))

  def startup = {

    val fileReadActor = system.actorOf(Props[FileReadActor])

    val remoteActor = system
      .actorFor("akka://WCMapReduceApp@10.20.108.237:2552/user/MapReduceActor");

    val actor = system.actorOf(Props(new ClientActor(remoteActor)))

    fileReadActor.tell(fileName, actor)
    
    Thread.sleep(100)

    remoteActor ! "DISPLAY_LIST"
  }

  def shutdown = {
    system.shutdown()
  }
}

object Client {
  def main(args: Array[String]): Unit = {

    val c = new Client

    c.startup

    Thread.sleep(4000)

    c.shutdown

  }

}