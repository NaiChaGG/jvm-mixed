package io.github.jojoti.examples.akka.kt.test

import akka.actor.AbstractActor
import akka.event.Logging

/**
 * @author JoJo Wang
 * Date: 2019/1/8
 * Time: 16:28
 */
class RoomPlayer(
    private val roomId: Long,
    private val userId: Long
) : AbstractActor() {

    private val log = Logging.getLogger(context.system, this)

    // 用于保存和恢复所有的用户信息
    override fun createReceive(): Receive {
        return this.receiveBuilder()
            .matchEquals("InitPlayer") {
                log.info(it + roomId)
                log.info(it + userId)
            }
            .build()
    }

    override fun postStop() {
        super.postStop()

        log.info("stop")
    }

}