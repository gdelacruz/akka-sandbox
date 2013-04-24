package test

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import akka.util.Timeout.durationToTimeout
import scala.collection.Seq
import scala.concurrent.Await
import akka.pattern.AskTimeoutException
import akka.actor.ActorLogging

class Reducer extends Actor with ActorLogging {

  def receive = {

    case x: Seq[Int] => log.info("Empece " + x); Thread.sleep(2000); log.info("Termine " + x); sender ! x.reduce(_ + _)
  }
}

class Delegator extends Actor with ActorLogging {
  val worker = context.actorOf(Props[Reducer])
  implicit val timeout: Timeout = 1 seconds

  def receive = {
    case _ =>
      try {
        println(Thread.currentThread().getName())
        worker ? (1 to 2) map (x => println("got " + x))
        log.info("Send 1")

        val future = worker ? (2 to 3)
        log.info("Send 2")

        worker ? (2 to 3) map (x => println("got " + x))
        log.info("Send 3")

        val result = Await.result(future, 10 seconds)

        log.info("Result " + result)

        log.info("Finish")
      } catch {
        case e: AskTimeoutException =>
          worker ? (4 to 5) map (x => println("got " + x))
          log.info("Send 4")

      }

  }
}

object futures {
  def main(args: Array[String]) {
    val system = ActorSystem()
    val delegator = system.actorOf(Props[Delegator])
    delegator ! "Start"
    Thread.sleep(10000)
    system.shutdown
  }

}