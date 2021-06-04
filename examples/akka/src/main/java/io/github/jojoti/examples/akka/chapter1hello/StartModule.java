package io.github.jojoti.examples.akka.chapter1hello;

import akka.actor.*;
import akka.event.LoggingAdapter;
import akka.event.Logging;

/**
 * Created by @JoJo Wang on 2017/8/7.
 */
public class StartModule {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Hello");
        ActorRef a = system.actorOf(Props.create(HelloWorld.class), "helloWorld");
//        system.actorOf(Props.create(Terminator.class, a), "terminator");
    }

    public static class Terminator extends UntypedActor {

        private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        private final ActorRef ref;

        public Terminator(ActorRef ref) {
            this.ref = ref;
            getContext().watch(ref);
        }

        @Override
        public void onReceive(Object msg) {
            if (msg instanceof Terminated) {
                log.info("{} has terminated, shutting down system", ref.path());
                getContext().system().terminate();
            } else {
                unhandled(msg);
            }
        }

    }
}
