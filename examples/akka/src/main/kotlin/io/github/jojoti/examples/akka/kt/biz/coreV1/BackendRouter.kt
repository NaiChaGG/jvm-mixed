package io.github.jojoti.examples.akka.kt.biz.coreV1

import akka.actor.ActorRef
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorRequest

/**
 * @author JoJo Wang
 * Date: 2019/1/3
 * Time: 21:50
 */
interface BackendRouter {

    val routers: Map<String, ActorRef>

    fun onForward(r: UserActorRequest)

}

object BackendTreeNode {
    const val ROUTER = "actor-tree-node-backend-router"
}