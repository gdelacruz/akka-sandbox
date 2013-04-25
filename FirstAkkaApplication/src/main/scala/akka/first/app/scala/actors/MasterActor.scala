package akka.first.app.mapreduce.actors

import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.first.app.scala.Result
import akka.first.app.scala.actors.MapActor
import akka.first.app.scala.actors.ReduceActor
import akka.routing.RoundRobinRouter
import akka.first.app.scala.actors.AggregateActor
import akka.first.app.scala.MapData
import akka.first.app.scala.ReduceData

class MasterActor extends Actor {
  val mapActor = context.actorOf(Props[MapActor].withRouter(RoundRobinRouter(nrOfInstances = 5)), name = "map")
  val reduceActor = context.actorOf(Props[ReduceActor].withRouter(RoundRobinRouter(nrOfInstances = 5)), name = "reduce")
  val aggregateActor = context.actorOf(Props[AggregateActor], name = "aggregate")

  def receive: Receive = {
    case line: String =>
      mapActor ! line
    case mapData: MapData =>
      reduceActor ! mapData
    case reduceData: ReduceData =>
      aggregateActor ! reduceData
    case r:Result =>
      aggregateActor forward r
  }
}
