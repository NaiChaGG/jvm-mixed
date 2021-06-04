package io.github.jojoti.examples.akka.chapter1hello;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.logging.Logger;

/**
 * Created by @JoJo Wang on 2017/8/7.
 */
public class HelloWorld extends UntypedActor {

    private Logger logger = Logger.getLogger(HelloWorld.class.getName());

    @Override
    public void preStart() {
        // create the greeter actor
        final ActorRef greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        // tell it to perform the greeting
        greeter.tell(Greeter.Msg.GREET, getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        if (msg == Greeter.Msg.DONE) {
            // when the greeter is done, stop this actor and with it the application
            logger.info("greeter message execute done.");
            getContext().stop(getSelf());
        } else
            unhandled(msg);
    }
}