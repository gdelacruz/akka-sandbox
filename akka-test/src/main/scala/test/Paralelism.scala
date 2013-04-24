package test

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinRouter

class Worker extends Actor{
  
  def receive = {
    case x => println("Pinging!! "+ x);
  }
}


object Paralelism {
  def main(args: Array[String]) {
	  val system = ActorSystem()
	  val worker = system.actorOf(Props[Worker].withRouter(RoundRobinRouter(5)))
	  
	  for (i <- 1 to 100)  worker ! i 

	  system.shutdown
}
  
}