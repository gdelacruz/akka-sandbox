package test

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinRouter
import akka.actor.PoisonPill

class Pinger extends Actor{
  
  def receive = {
    case _ => println("Pinging!!"); sender ! "Ping!"
  }
}

class Ponger extends Actor{
  
  def receive = {
    case _ => println("Ponging!!"); sender ! "Pong!"
  }
}


object PingPong {
  def main(args: Array[String]) {
	  val system = ActorSystem()
	  val pinger = system.actorOf(Props[Pinger])
	  val ponger = system.actorOf(Props[Ponger])
	  //pinger.!("Ping!")(ponger)
	  pinger.tell("Ping!",ponger)
	  Thread.sleep(10000)
	  pinger ! PoisonPill
	  ponger ! PoisonPill
	  Thread.sleep(1000)
	  system.shutdown
}
  
}