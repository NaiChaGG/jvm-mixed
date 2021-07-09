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

package io.github.jojoti.grpcstartersbexamples;

import io.github.jojoti.grpcstartersb.GRpcPrimaryService;
import io.github.jojoti.grpcstartersb.GRpcServiceInterceptors;
import io.github.jojoti.grpcstartersb.Trailers;
import io.github.jojoti.grpcstartersbram.RAM;
import io.grpc.stub.StreamObserver;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcPrimaryService
@GRpcServiceInterceptors(interceptors = App1Interceptor.class)
public class Foo2Handler extends Foo2Grpc.Foo2ImplBase {

    public Foo2Handler() {

    }

    @Override
    @RAM(groupId = 1, name = "1")
    public void bar(Hello2.BarRequest2 request, StreamObserver<Hello2.BarResponse2> responseObserver) {
        responseObserver.onError(Trailers.newErrorCode(1));
        super.bar(request, responseObserver);
    }

}