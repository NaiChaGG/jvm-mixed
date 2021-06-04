package io.github.jojoti.examples.akka.chapter3group;

/**
 * Created by @JoJo Wang on 2017/8/15.
 */

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.testkit.javadsl.TestKit;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

class Device extends AbstractActor {
    final String groupId;
    final String deviceId;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    Optional<Double> lastTemperatureReading = Optional.empty();

    public Device(String groupId, String deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public static Props props(String groupId, String deviceId) {
        return Props.create(Device.class, groupId, deviceId);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("test");
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));
        deviceActor.tell(new ReadTemperature(42L), probe.getRef());
        RespondTemperature response = probe.expectMsgClass(RespondTemperature.class);

        assertEquals(42L, response.requestId);
        assertEquals(Optional.empty(), response.value);
    }

    @Override
    public void preStart() {
        log.info("Device actor {}-{} started", groupId, deviceId);
    }

    @Override
    public void postStop() {
        log.info("Device actor {}-{} stopped", groupId, deviceId);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ReadTemperature.class, r -> {
                    getSender().tell(new RespondTemperature(r.requestId, lastTemperatureReading), getSelf());
                })
                .build();
    }

    @Test
    public void testReplyWithEmptyReadingIfNoTemperatureIsKnown() {
        ActorSystem system = ActorSystem.create("test");
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));
        deviceActor.tell(new ReadTemperature(42L), probe.getRef());
        RespondTemperature response = probe.expectMsgClass(RespondTemperature.class);

        assertEquals(42L, response.requestId);
        assertEquals(Optional.empty(), response.value);
    }

    public static final class ReadTemperature {
        long requestId;

        public ReadTemperature(long requestId) {
            this.requestId = requestId;
        }
    }

    public static final class RespondTemperature {
        long requestId;
        Optional<Double> value;

        public RespondTemperature(long requestId, Optional<Double> value) {
            this.requestId = requestId;
            this.value = value;
        }
    }

    public static final class RecordTemperature {
        final double value;

        public RecordTemperature(double value) {
            this.value = value;
        }
    }

}
