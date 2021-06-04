package io.github.jojoti.testing.akka.chapter4;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.testkit.javadsl.TestKit;
import io.github.jojoti.testing.akka.chapter4.Device;
import io.github.jojoti.testing.akka.chapter4.DeviceGroup;
import io.github.jojoti.testing.akka.chapter4.DeviceManager;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Created by @JoJo Wang on 2017/8/15.
 */
public class MainTest {
    ActorSystem system = ActorSystem.create("test");

    @Test
    public void testReplyToRegistrationRequests() {
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

        deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "device"), probe.getRef());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        assertEquals(deviceActor, probe.getLastSender());
    }

    @Test
    public void testIgnoreWrongRegistrationRequests() {
        TestKit probe = new TestKit(system);
        ActorRef deviceActor = system.actorOf(Device.props("group", "device"));

        deviceActor.tell(new DeviceManager.RequestTrackDevice("wrongGroup", "device"), probe.getRef());
        probe.expectNoMsg();

        deviceActor.tell(new DeviceManager.RequestTrackDevice("group", "wrongDevice"), probe.getRef());
        probe.expectNoMsg();
    }

    @Test
    public void testListActiveDevices() {
        TestKit probe = new TestKit(system);
        ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.getRef());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.getRef());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

        groupActor.tell(new DeviceGroup.RequestDeviceList(0L), probe.getRef());
        DeviceGroup.ReplyDeviceList reply = probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);

        assertEquals(0L, reply.requestId);
        assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.ids);
    }

    @Test
    public void testListActiveDevicesAfterOneShutsDown() {
        TestKit probe = new TestKit(system);
        ActorRef groupActor = system.actorOf(DeviceGroup.props("group"));

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device1"), probe.getRef());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);
        ActorRef toShutDown = probe.getLastSender();

        groupActor.tell(new DeviceManager.RequestTrackDevice("group", "device2"), probe.getRef());
        probe.expectMsgClass(DeviceManager.DeviceRegistered.class);

        groupActor.tell(new DeviceGroup.RequestDeviceList(0L), probe.getRef());
        DeviceGroup.ReplyDeviceList reply = probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
        assertEquals(0L, reply.requestId);
        assertEquals(Stream.of("device1", "device2").collect(Collectors.toSet()), reply.ids);

        probe.watch(toShutDown);
        toShutDown.tell(PoisonPill.getInstance(), ActorRef.noSender());
        probe.expectTerminated(toShutDown);

        // using awaitAssert to retry because it might take longer for the groupActor
        // to see the Terminated, that order is undefined
        probe.awaitAssert(() -> {
            groupActor.tell(new DeviceGroup.RequestDeviceList(1L), probe.getRef());
            DeviceGroup.ReplyDeviceList r =
                    probe.expectMsgClass(DeviceGroup.ReplyDeviceList.class);
            assertEquals(1L, r.requestId);
            assertEquals(Stream.of("device2").collect(Collectors.toSet()), r.ids);
            return null;
        });
    }

}
