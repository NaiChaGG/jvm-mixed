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

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class Device1 extends AbstractActor {
    final String groupId;
    final String deviceId;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    Optional<Double> lastTemperatureReading = Optional.empty();

    public Device1(String groupId, String deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public static Props props(String groupId, String deviceId) {
        return Props.create(Device.class, groupId, deviceId);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("test");
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device1.props("group", "device"));

        deviceActor.tell(new RecordTemperature(1L, 24.0), probe.getRef());
        assertEquals(1L, probe.expectMsgClass(TemperatureRecorded.class).requestId);

        deviceActor.tell(new ReadTemperature(2L), probe.getRef());
        RespondTemperature response1 = probe.expectMsgClass(RespondTemperature.class);
        assertEquals(2L, response1.requestId);
        assertEquals(Optional.of(24.0), response1.value);

        deviceActor.tell(new RecordTemperature(3L, 55.0), probe.getRef());
        assertEquals(3L, probe.expectMsgClass(TemperatureRecorded.class).requestId);

        deviceActor.tell(new ReadTemperature(4L), probe.getRef());
        RespondTemperature response2 = probe.expectMsgClass(RespondTemperature.class);
        assertEquals(4L, response2.requestId);
        assertEquals(Optional.of(55.0), response2.value);
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
                .match(RecordTemperature.class, r -> {
                    log.info("Recorded temperature reading {} with {}", r.value, r.requestId);
                    lastTemperatureReading = Optional.of(r.value);
                    getSender().tell(new TemperatureRecorded(r.requestId), getSelf());
                })
                .match(ReadTemperature.class, r -> {
                    getSender().tell(new RespondTemperature(r.requestId, lastTemperatureReading), getSelf());
                })
                .build();
    }

    public static final class RecordTemperature {
        final long requestId;
        final double value;

        public RecordTemperature(long requestId, double value) {
            this.requestId = requestId;
            this.value = value;
        }
    }

    public static final class TemperatureRecorded {
        final long requestId;

        public TemperatureRecorded(long requestId) {
            this.requestId = requestId;
        }
    }

    public static final class ReadTemperature {
        final long requestId;

        public ReadTemperature(long requestId) {
            this.requestId = requestId;
        }
    }

    public static final class RespondTemperature {
        final long requestId;
        final Optional<Double> value;

        public RespondTemperature(long requestId, Optional<Double> value) {
            this.requestId = requestId;
            this.value = value;
        }
    }
}