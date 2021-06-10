package io.github.jojoti.grpcstatersbexamples

import io.github.jojoti.grpcstatersb.GRpcPrivateService
import io.github.jojoti.grpcstatersbexamples.FooGrpcKt
import io.github.jojoti.grpcstatersbexamples.Hello
import kotlin.coroutines.CoroutineContext

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcPrivateService
open class FooHandler : FooGrpcKt.FooCoroutineImplBase() {

    override suspend fun bar(request: Hello.BarRequest): Hello.BarResponse {
        return Hello.BarResponse.newBuilder().build();
    }
}