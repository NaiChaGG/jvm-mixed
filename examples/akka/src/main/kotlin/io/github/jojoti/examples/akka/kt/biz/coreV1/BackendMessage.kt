package io.github.jojoti.examples.akka.kt.biz.coreV1

import java.io.Serializable

/**
 * @author JoJo Wang
 * Date: 2019/1/3
 * Time: 21:47
 */
interface BackendMessage : Serializable

abstract class BackendMessageEntity(
    val seqId: Long
)