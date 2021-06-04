package io.github.jojoti.examples.akka.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static akka.pattern.Patterns.ask;

public class TransformationFrontendMain {

    public static void main(String[] args) {
        // Override the configuration of the port when specified as program argument
        final String port = args.length > 0 ? args[0] : "0";
        final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
                withFallback(ConfigFactory.parseString("akka.io.github.jojoti.testing.akka.cluster.roles = [frontend]")).
                withFallback(ConfigFactory.load("cluster1"));

        ActorSystem system = ActorSystem.create("ClusterSystem", config);

        final ActorRef frontend = system.actorOf(
                Props.create(TransformationFrontend.class), "frontend");

        final FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);
        final Timeout timeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));
        final ExecutionContext ec = system.dispatcher();
        final AtomicInteger counter = new AtomicInteger();

        ask(frontend, new TransformationMessages.TransformationJob("hello-" + counter.incrementAndGet()),
                timeout).onSuccess(new OnSuccess<Object>() {
            public void onSuccess(Object result) {
                System.out.println(result);
            }
        }, ec);

        system.scheduler().schedule(interval, interval, new Runnable() {
            public void run() {
                ask(frontend,
                        new TransformationMessages.TransformationJob("hello-" + counter.incrementAndGet()),
                        timeout).onSuccess(new OnSuccess<Object>() {
                    public void onSuccess(Object result) {
                        System.out.println(result);
                    }
                }, ec);
            }

        }, ec);

    }
}
