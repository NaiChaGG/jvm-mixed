package io.github.jojoti.grpcstartersbexamples

import io.github.jojoti.grpcstartersb.GRpcPrivateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @rf https://rengwuxian.com/kotlin-coroutines-2/
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcPrivateService
open class FooHandler : FooGrpcKt.FooCoroutineImplBase() {

    override suspend fun bar(request: Hello.BarRequest): Hello.BarResponse =
        withContext(Dispatchers.IO) {
            Hello.BarResponse.newBuilder().build();
        }

}