package test

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.routing.RoundRobinRouter
import akka.actor.DeadLetter

case class Greeting(who: String, time: Int)

class CpuActor extends Actor with ActorLogging {
  

  override def preStart() = {
    log.info("starting CpuActor ")
  }

  def receive = {
    case Greeting(who, time) ⇒ { Thread.sleep(1000 * time); log.info("CPU ACTOR" + who) }
  }
}

class IOActor extends Actor with ActorLogging {


  override def preStart() = {
    log.info("starting ")
  }

  def receive = {
    case Greeting(who, time) ⇒ { Thread.sleep(1000 * time); log.info("IO ACTOR " + who) }
  }
}


object HelloWorld {

  def main(args: Array[String]) {
    val system = ActorSystem("MySystem")
    //println(system.settings)

    val cpu = system.actorOf(
      Props[CpuActor]
        .withRouter(RoundRobinRouter(5))
        .withDispatcher("a.b.cpu-fork-dispatcher")
        , name = "cpu")
        
    val io = system.actorOf(
      Props[IOActor]
        .withRouter(RoundRobinRouter(30))
        .withDispatcher("a.b.io-fork-dispatcher")
        , name = "io")
        
    //val greeter2 = system.actorOf(Props[GreetingActor].withDispatcher("a.b.thread-dispatcher"), name = "greeter2")

    (1 to 5000) foreach (x => {
      val msg = Greeting("Charlie Parker ", 5)
      if (x % 2 == 0)
        cpu ! msg
      else
        io ! msg
    })
    

  }

}