package io.github.jojoti.examples.akka.kt.test

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.Terminated
import java.util.concurrent.atomic.AtomicLong

/**
 * @author JoJo Wang
 * Date: 2019/1/8
 * Time: 16:59
 */
class RoomAdmin : AbstractActor() {

    private val currentRoomId = AtomicLong(0)

    private val rooms = mutableMapOf<Long, ActorRef>()
    private val roomPlayers = mutableMapOf<Long, Map<Long, ActorRef>>()

    // gateway 就一定会有 /gateway/router/w0
    // gateway 就一定会有 /gateway/router/w1
    // gateway 就一定会有 /gateway/router/w2
    override fun createReceive(): Receive {
        return this.receiveBuilder()
            .match(CreateRoom::class.java) {
                val roomId = currentRoomId.getAndIncrement()

                val roomActor = context.actorOf(
                    Props.create(Room::class.java, roomId, it.userIds),
                    "room/$roomId"
                )

                val players = mutableMapOf<Long, ActorRef>()

                for (userId in it.userIds) {
                    // 创建了一个 player actor
                    val playerActor = context.actorOf(
                        Props.create(RoomPlayer::class.java, roomId, userId),
                        "room/$roomId/players"
                    )

                    players[userId] = playerActor
                    roomActor.tell("I, am a player.", playerActor)
                }

                rooms[roomId] = context.watch(roomActor)

                roomPlayers[roomId] = players

                this.sender.tell(CreateRoomReply(roomId), this.self)
            }
            .match(RestartRoom::class.java) {
                //
                val roomActor = rooms[it.roomId]
                when (roomActor) {
                    null -> {
                        // ignore
                    }
                    else -> {
                        context.stop(roomActor)
                    }
                }
            }
            .match(Terminated::class.java) {
                // room | player 挂掉
                context.unwatch(sender)

                // 可以从路径解析出
                // sender.path()

            }
            .build()
    }

}

data class CreateRoom(
    val userIds: List<Long>
)

data class RestartRoom(
    val roomId: Long
)

data class RestartRoomPlayer(
    val roomId: Long,
    val userId: Long
)

data class CreateRoomReply(
    val roomId: Long
)