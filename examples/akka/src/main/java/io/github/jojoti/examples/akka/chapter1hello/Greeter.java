package io.github.jojoti.examples.akka.chapter1hello;

import akka.actor.UntypedActor;

import java.util.logging.Logger;

/**
 * Created by @JoJo Wang on 2017/8/7.
 */
public class Greeter extends UntypedActor {

    private Logger logger = Logger.getLogger(Greeter.class.getName());

    @Override
    public void onReceive(Object msg) {
        if (msg == Msg.GREET) {

            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(msg);
        }
    }

    public enum Msg {
        GREET, DONE
    }


}
