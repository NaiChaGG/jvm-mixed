/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jojoti.grpcstartersbexamples

//import io.github.jojoti.grpcstartersbram.RAM
import io.github.jojoti.grpcstartersb.GRpcAdminService
import io.github.jojoti.grpcstartersb.Trailers
import io.github.jojoti.grpcstartersbkt.Coroutines
import org.springframework.beans.factory.annotation.Autowired

/**
 * @rf https://rengwuxian.com/kotlin-coroutines-2/
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcAdminService
open class FooHandler : FooGrpcKt.FooCoroutineImplBase() {

    @Autowired
    lateinit var myService: MyService

    //    @RAM(value = RAM.RAMItem(groupId = 1, attrs = arrayOf(RAM.RAMAttr(key = "access", value = "1"))))
    override suspend fun bar(request: Hello.BarRequest): Hello.BarResponse =
        Coroutines.newSyncIo {
            throw Trailers.newErrorCode(111)
//                myService.foo()
//                Hello.BarResponse.newBuilder().build();
        }

}