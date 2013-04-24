package org.akka.essentials.supervisor.example2;

import java.util.concurrent.TimeUnit;

import org.akka.essentials.supervisor.example2.MyActorSystem2.Result;
import org.akka.essentials.supervisor.example2.SupervisorActor2;
import org.junit.Test;

import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.dispatch.Await;
import akka.pattern.Patterns;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import akka.testkit.TestProbe;
import akka.util.Duration;

public class SupervisorTest extends TestKit {
	static ActorSystem _system = ActorSystem.create("faultTolerance", ConfigFactory
			.load().getConfig("SupervisorSys"));
	TestActorRef<SupervisorActor2> supervisor = TestActorRef.apply(new Props(
			SupervisorActor2.class), _system);

	public SupervisorTest() {
		super(_system);
	}

	@Test
	public void successTest() throws Exception {
		supervisor.tell(Integer.valueOf(8));

		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(8));
	}

	@Test
	public void resumeTest() throws Exception {
		supervisor.tell(Integer.valueOf(-8));
		Thread.sleep(5000);
		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(8));
	}

	@Test
	public void restartTest() throws Exception {
		supervisor.tell(null);

		Integer result = (Integer) Await.result(
				Patterns.ask(supervisor, new Result(), 5000),
				Duration.create(5000, TimeUnit.MILLISECONDS));

		assert result.equals(Integer.valueOf(0));
	}

	@Test
	public void stopTest() throws Exception {

		ActorRef workerActor1 = supervisor.underlyingActor().workerActor1;
		ActorRef workerActor2 = supervisor.underlyingActor().workerActor2;
		
		TestProbe probe1 = new TestProbe(_system);
		TestProbe probe2 = new TestProbe(_system);
		probe1.watch(workerActor1);
		probe2.watch(workerActor2);

		supervisor.tell(String.valueOf("Do Something"));

		probe1.expectMsg(new Terminated(workerActor1));
		probe2.expectMsg(new Terminated(workerActor2));
	}
}
