package io.github.jojoti.examples.akka.kt.biz.core

import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorRequest
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorResponse
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorResponseProtoError

object PbExchangeMessage {

    fun errUriNotFound(seqId: Long): UserActorResponse {
        return UserActorResponse.newBuilder()
            .setSeqId(seqId)
            .setServerTime(System.currentTimeMillis())
            .setErr(UserActorResponseProtoError.REMOTING_URI_NOT_FOUND)
            .build()
    }

    fun errUriNotFound(request: UserActorRequest): UserActorResponse {
        return errUriNotFound(request.seqId)
    }

}

object PbExchangeMessageUtils {


    fun err(seqId: Long, err: Int, msg: String, vararg msgs: String) {

    }

}