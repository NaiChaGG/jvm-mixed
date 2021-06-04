package io.github.jojoti.examples.akka.kt.biz.coreV1

import com.google.protobuf.ByteString
import com.google.protobuf.GeneratedMessageV3

/**
 * @author JoJo Wang
 * Date: 2019/1/3
 * Time: 21:26
 */
interface Backend<RequestBody, ResponseBody : GeneratedMessageV3> {

    val uri: String

    fun onRequest(seqId: Long, from: String, userId: Long, body: RequestBody)

    fun parsedRequestBody(body: ByteString): RequestBody

    fun reply(seqId: Long, body: ResponseBody)

}