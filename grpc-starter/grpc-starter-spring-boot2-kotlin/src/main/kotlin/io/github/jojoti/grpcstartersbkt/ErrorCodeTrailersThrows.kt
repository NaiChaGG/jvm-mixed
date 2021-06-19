package io.github.jojoti.grpcstartersbkt

import io.github.jojoti.grpcstartersb.ErrorCodeTrailers
import io.grpc.StatusException

/**
 * 参考 grpc kotlin 生成的 协程实现，要返回错误，需要使用 error 的模式来返回
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
object ErrorCodeTrailersThrows {

    fun newStatus(error: Int): StatusException {
        throw ErrorCodeTrailers.newStatus(error)
    }

}