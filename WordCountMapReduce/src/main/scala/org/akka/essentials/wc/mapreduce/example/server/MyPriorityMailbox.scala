package org.akka.essentials.wc.mapreduce.example.server;

import com.typesafe.config.Config

import akka.actor.ActorSystem
import akka.actor.PoisonPill
import akka.dispatch.PriorityGenerator
import akka.dispatch.UnboundedPriorityMailbox

class MyPriorityMailbox(settings: ActorSystem.Settings, config: Config)
  extends UnboundedPriorityMailbox(
    // Creating a new PriorityGenerator,
    PriorityGenerator {
      case message: String if (message == "DISPLAY_LIST") => 2
      // last if possible
      case PoisonPill => 3
      case _ => 1
    })