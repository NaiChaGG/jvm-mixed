package io.github.jojoti.examples.akka.kt.biz.coreV1

import akka.actor.AbstractActor
import akka.actor.ActorRef
import io.github.jojoti.examples.akka.kt.biz.core.PbExchangeMessage
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.ActorGetAllRoute
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.ActorGetAllRouteReply
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.ActorRouteEntity
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorRequest
import java.util.*

/**
 * @author JoJo Wang
 * Date: 2019/1/3
 * Time: 21:21
 */
abstract class BackendRouterActor : AbstractActor(), BackendRouter {

    override val routers = LinkedHashMap<String, ActorRef>()

    override fun onForward(r: UserActorRequest) {
        val childNode = routers[r.uri]
            ?: return this.sender.tell(
                PbExchangeMessage.errUriNotFound(r),
                self
            )
        childNode.forward(r, context)
    }

    override fun createReceive(): AbstractActor.Receive {
        return receiveBuilder()
            .match(UserActorRequest::class.java, this::onForward)
            .match(ActorGetAllRoute::class.java) {
                val reply = ActorGetAllRouteReply.newBuilder()
                    .setSeqId(it.seqId)

                routers.keys.forEach { routerUri: String ->
                    reply.addEntities(ActorRouteEntity.newBuilder().setUri(routerUri))
                }
                // 回复 route 列表
                this.sender.tell(reply, this.self)
            }
            .match(NodeRouteRegister::class.java) {
                routers[it.uri] = this.sender
                // 回复路由注册成功
                this.sender.tell(NodeRouterRegisterReply, this.self)
            }.match(NodeRouteRemove::class.java) {
                routers.remove(it.uri)
                // 回复路由移除成功
                this.sender.tell(NodeRouterRemoveReply, this.self)
            }.matchAny(this::unhandled)
            .build()
    }

    public data class NodeRouteRegister(
        val uri: String
    )

    public data class NodeRouteRemove(
        val uri: String
    )

    public object NodeRouterRegisterReply

    public object NodeRouterRemoveReply
}