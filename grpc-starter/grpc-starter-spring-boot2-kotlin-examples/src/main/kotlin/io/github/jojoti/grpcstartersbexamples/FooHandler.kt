package io.github.jojoti.grpcstartersbexamples

import io.github.jojoti.grpcstartersb.GRpcPrivateService
import io.github.jojoti.grpcstartersbkt.ErrorCodeTrailersThrows
import io.github.jojoti.grpcstartersbram.RAM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired

/**
 * @rf https://rengwuxian.com/kotlin-coroutines-2/
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcPrivateService
open class FooHandler : FooGrpcKt.FooCoroutineImplBase() {

    @Autowired
    lateinit var myService: MyService

    @RAM(value = RAM.RAMItem(groupId = 1, attrs = arrayOf(RAM.RAMAttr(key = "access", value = "1"))))
    override suspend fun bar(request: Hello.BarRequest): Hello.BarResponse =
            withContext(Dispatchers.IO) {
                ErrorCodeTrailersThrows.newStatus(1)
                myService.foo()
                Hello.BarResponse.newBuilder().build();
            }

}