package io.github.jojoti.examples.akka.kt.akka_test.api

import io.github.jojoti.testing.akka.kt.akka_test.proto.LoginRequest
import io.github.jojoti.testing.akka.kt.akka_test.proto.LoginResponse
import com.google.protobuf.ByteString
import io.github.jojoti.examples.akka.kt.biz.coreV1.AbstractBackendActor

/**
 * @author JoJo Wang
 * Date: 2019/1/3
 * Time: 23:17
 */
class LoginActor : AbstractBackendActor<LoginRequest, LoginResponse>() {

    override fun onRequest(seqId: Long, from: String, userId: Long, body: LoginRequest) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parsedRequestBody(body: ByteString): LoginRequest {
        return LoginRequest.parseFrom(body)
    }

    override val uri: String = "/login"

}