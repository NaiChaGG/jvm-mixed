package io.github.jojoti.examples.akka.chapter1actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by @JoJo Wang on 2017/8/7.
 */
public class TargetActor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object o) throws Throwable {
        switch ((String) o) {
            case "ping1":
                logger.info("target actor receive ping1." + System.nanoTime());
                getSender().tell("ok1", getSelf());
                break;
            case "ping2":
                logger.info("target actor receive ping2." + System.nanoTime());
                getSender().tell("ok2", getSelf());
                break;
            case "ping3":
                logger.info("target actor receive ping3." + System.nanoTime());
                getSender().tell("ok3", getSelf());
                break;
            case "ping4":
                logger.info("target actor receive ping4." + System.nanoTime());
                getSender().tell("ok4", getSelf());
                break;

            default:
                unhandled(o);
        }
    }

}
