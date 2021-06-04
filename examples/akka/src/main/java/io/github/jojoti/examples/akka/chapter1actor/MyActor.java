package io.github.jojoti.examples.akka.chapter1actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by @JoJo Wang on 2017/8/7.
 */
public class MyActor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() throws Exception {
//        // create the greeter actor
//        final ActorRef targetActor = getContext().actorOf(Props.create(TargetActor.class));
//        // tell it to perform the greeting
//        logger.info("my actor send ping.");
//        targetActor.tell("ping", getSelf());
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        switch ((String) o) {
            case "ok1":
                logger.info("my actor receive ok1.------" + System.nanoTime());
                break;
            case "ok2":
                logger.info("my actor receive ok2.------" + System.nanoTime());
                break;
            case "ok3":
                logger.info("my actor receive ok3.------" + System.nanoTime());
                break;
            case "ok4":
                logger.info("my actor receive ok4.------" + System.nanoTime());
                getContext().stop(getSelf());
                break;
            case "actor":
                logger.info("my actor receive actor.");
                break;
            case "root":
                logger.info("root.");
                break;
            default:
                unhandled(o);
        }
    }


}
