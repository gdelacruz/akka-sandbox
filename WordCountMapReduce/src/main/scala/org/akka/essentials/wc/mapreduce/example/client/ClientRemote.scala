package org.akka.essentials.wc.mapreduce.example.client

import org.akka.essentials.wc.mapreduce.example.server.MapReduceActor
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.kernel.Bootable
import akka.actor.AddressFromURIString
import akka.actor.Deploy
import akka.remote.RemoteScope

class ClientRemote extends Bootable {

  val fileName = "/home/ger/develop/git/akka-sandbox/WordCountMapReduce/src/main/resources/Othello.txt"

  val system = ActorSystem("ClientApplication", ConfigFactory.load().getConfig("WCMapReduceClientRemoteApp"))

  def startup = {

    val fileReadActor = system.actorOf(Props[FileReadActor])

    val remoteActor = system.actorOf(Props[MapReduceActor], "MapReduceActor");
    
    val actor = system.actorOf(Props(new ClientActor(remoteActor)))

    fileReadActor.tell(fileName, actor)

    Thread.sleep(1000)

    remoteActor ! "DISPLAY_LIST"
  }

  def shutdown = {
    system.shutdown()
  }
}

object ClientRemote {
  def main(args: Array[String]): Unit = {

    val c = new ClientRemote

    c.startup

    Thread.sleep(4000)

    c.shutdown

  }

}