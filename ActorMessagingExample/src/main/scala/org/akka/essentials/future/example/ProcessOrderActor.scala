package org.akka.essentials.future.example
import akka.actor.Actor
import akka.actor.Props
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout
import akka.pattern.ask
import akka.actor.actorRef2Scala
import scala.concurrent.duration._
import scala.concurrent.Future



class ProcessOrderActor extends Actor {

  implicit val timeout = Timeout(5.seconds)
  val orderActor = context.actorOf(Props[OrderActor])
  val addressActor = context.actorOf(Props[AddressActor])
  val orderAggregateActor = context.actorOf(Props[OrderAggregateActor])

  def receive = {
    case userId: Integer =>
      //Tuve que poner este import. Lo saque de la doc de scala 2.1. No entiendo muy bien porque
      //http://doc.akka.io/docs/akka/snapshot/scala/futures.html
      import context.dispatcher
      
      val aggResult: Future[OrderHistory] =
        for {
          order <- (orderActor ? userId).mapTo[Order] // call pattern directly
          address <- (addressActor ? userId) mapTo manifest[Address] // call by implicit conversion
        } yield OrderHistory(order, address)
      aggResult pipeTo orderAggregateActor
  }
}