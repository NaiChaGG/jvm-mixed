package io.github.jojoti.grpcstartersbkt

import io.github.jojoti.grpcstartersb.HeaderErrorCode
import io.grpc.StatusRuntimeException

/**
 * 参考 grpc kotlin 生成的 协程实现，要返回错误，需要使用 error 的模式来返回
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
object HeaderErrorHeaderThrows {

    fun newCode(error: Int): StatusRuntimeException {
        throw HeaderErrorCode.newCode(error)
    }

}