package io.github.jojoti.grpcstartersbkt

import io.github.jojoti.grpcstartersb.Trailers
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Callable

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
object Coroutines {

    private val log: Logger = LoggerFactory.getLogger(Coroutines.javaClass)

    suspend fun <T> newSyncIo(block: Callable<T>): T {
        return withContext(Dispatchers.IO) {
            val rs = try {
                // grpc kotlin handler 报错，会导致错误丢失，不给客户端返回数据，服务器也要一次一次的捕捉错误
                block.call()
            } catch (e: StatusException) {
                throw e
            } catch (e: StatusRuntimeException) {
                throw e
            } catch (e: Exception) {
                log.error("trace error", e)
                throw Trailers.newErrorTraces(e)
            }
            rs
        }
    }

}