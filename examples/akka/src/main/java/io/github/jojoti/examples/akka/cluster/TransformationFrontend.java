package io.github.jojoti.examples.akka.cluster;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;

import java.util.ArrayList;
import java.util.List;

public class TransformationFrontend extends AbstractActor {

    List<ActorRef> backends = new ArrayList<>();
    int jobCounter = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TransformationMessages.TransformationJob.class, job -> backends.size() < 4, job -> {
                    sender().tell(new TransformationMessages.JobFailed("Service unavailable, try again later" + backends.size(), job),
                            sender());
                })
                .match(TransformationMessages.TransformationJob.class, job -> {
                    jobCounter++;
                    backends.get(jobCounter % backends.size())
                            .forward(job, getContext());
                })
                .matchEquals(TransformationMessages.BACKEND_REGISTRATION, message -> {
                    getContext().watch(sender());
                    backends.add(sender());
                })
                .match(Terminated.class, terminated -> {
                    backends.remove(terminated.getActor());
                })
                .build();
    }
}
