package io.github.jojoti.examples.akka.chapter1actor;

import akka.actor.*;

/**
 * Created by @JoJo Wang on 2017/8/7.
 */
public class MainTest {


    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("test");

        final ActorRef myActor1 = system.actorOf(Props.create(MyActor.class), "myActor");
        final ActorRef targetActor1 = system.actorOf(Props.create(TargetActor.class), "targetActor");
        targetActor1.tell("ping1", myActor1);
        targetActor1.tell("ping2", myActor1);
        targetActor1.tell("ping3", myActor1);
//        targetActor1.tell("ping4", myActor1);

        final ActorSelection myActor2 = system.actorSelection("user/myActor");
        myActor2.tell("root", targetActor1);

//        final ActorSelection targetActor2 = system.actorSelection("targetActor");
//        targetActor2.tell("ping1", myActor1);
//        targetActor2.tell("ping2", myActor1);
//        targetActor2.tell("ping3", myActor1);
//        targetActor2.tell("ping4", myActor1);
//
    }

}