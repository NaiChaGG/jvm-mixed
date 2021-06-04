package io.github.jojoti.examples.akka.kt.biz.core

import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorRequest

/**
 * @author JoJo Wang
 * Date: 2019/1/7
 * Time: 12:33
 */
interface FrontendRouter {

    fun dispatching(request: UserActorRequest)

}