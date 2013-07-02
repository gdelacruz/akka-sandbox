package org.akka.essentials.wc.mapreduce.example.server

import akka.actor.Actor
import akka.actor.Props
import akka.actor.actorRef2Scala
import akka.routing.RoundRobinRouter

class MapReduceActor extends Actor {

  val no_of_map_workers = 5

  val no_of_reduce_workers = 5

  // create the aggregate Actor
  val aggregateActor = context.actorOf(Props[AggregateActor]);

  // create the list of reduce Actors
  val reduceRouter = context.actorOf(Props(new ReduceActor(aggregateActor)).withRouter(new RoundRobinRouter(no_of_reduce_workers)));

  // create the list of map Actors
  val mapRouter = context.actorOf(Props(new MapActor(reduceRouter)).withRouter(new RoundRobinRouter(no_of_map_workers)));

  def receive = {
    case message: String if (message == "DISPLAY_LIST") =>
      System.out.println("Got Display Message");
      aggregateActor.tell(message, sender);
    case message =>
      mapRouter ! message;

  }
}