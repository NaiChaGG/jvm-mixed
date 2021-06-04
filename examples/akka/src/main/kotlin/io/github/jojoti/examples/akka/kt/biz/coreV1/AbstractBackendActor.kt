package io.github.jojoti.examples.akka.kt.biz.coreV1

import akka.actor.AbstractActor
import akka.japi.pf.ReceiveBuilder
import com.google.protobuf.GeneratedMessageV3
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorRequest
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorResponse
import java.util.*

/**
 * @author JoJo Wang
 * Date: 2019/1/3
 * Time: 21:21
 */
abstract class AbstractBackendActor<RequestBody, ResponseBody : GeneratedMessageV3> : AbstractActor(),
    Backend<RequestBody, ResponseBody> {

    override fun reply(seqId: Long, body: ResponseBody) {
        this.sender.tell(
            UserActorResponse.newBuilder()
                .setSeqId(seqId)
                .setServerTime(System.currentTimeMillis())
                .setBody(body.toByteString())
                .build(), self
        )
    }

    final override fun createReceive(): AbstractActor.Receive {
        val builder = receiveBuilder()

        builder.match(UserActorRequest::class.java) {

            this.onRequest(it.seqId, it.from, it.userId, this.parsedRequestBody(it.body))
        }.matchAny(this::unhandled)

        this.matchMore(builder)

        return builder.build()
    }

    // 开放处该 actor 的其它api匹配
    open fun matchMore(builder: ReceiveBuilder) {
        // nothing to do
    }

    override fun preStart() {
        super.preStart()
        this.context.system.actorSelection(BackendTreeNode.ROUTER)
            .tell(BackendRouterActor.NodeRouteRegister(uri), self)

    }

    override fun postStop() {
        super.postStop()
        this.context.system.actorSelection(BackendTreeNode.ROUTER)
            .tell(BackendRouterActor.NodeRouteRemove(uri), self)
    }

    override fun preRestart(reason: Throwable, message: Optional<Any>) {
        super.preRestart(reason, message)
        this.context.system.actorSelection(BackendTreeNode.ROUTER)
            .tell(BackendRouterActor.NodeRouteRegister(uri), self)
        // 记录日志
    }

}