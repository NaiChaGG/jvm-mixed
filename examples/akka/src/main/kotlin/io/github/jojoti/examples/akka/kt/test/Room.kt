package io.github.jojoti.examples.akka.kt.test

import akka.actor.AbstractActor
import akka.event.Logging

/**
 * @author JoJo Wang
 * Date: 2019/1/8
 * Time: 16:22
 */
class Room(
    private val roomId: Long,
    private val userIds: List<Long>
) : AbstractActor() {

    private val log = Logging.getLogger(context.system, this)

    // 保存房间所有的用户信息
    override fun createReceive(): Receive {
        return this.receiveBuilder()
            .matchEquals("InitRoom") {
                log.info(it + roomId)
                log.info(it + userIds)
            }
            .build()
    }

}