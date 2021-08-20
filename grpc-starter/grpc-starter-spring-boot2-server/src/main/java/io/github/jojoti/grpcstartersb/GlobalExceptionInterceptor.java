/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
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

package io.github.jojoti.grpcstartersb;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局异常处理
 *
 * <p>
 * https://stackoverflow.com/questions/39797142/how-to-add-global-exception-interceptor-in-grpc-server
 * https://github.com/grpc/grpc-java/issues/1552
 * https://skyao.io/learning-grpc/server/status/exception_process.html
 * https://stackoverflow.com/questions/57123965/grpc-response-headers-based-on-the-incoming-value
 * https://grpc.github.io/grpc-java/javadoc/io/grpc/util/TransmitStatusRuntimeExceptionInterceptor.html
 * https://github.com/dconnelly/grpc-error-example/blob/master/src/main/java/example/Errors.java
 * https://github.com/grpc/grpc-java/issues/681
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public final class GlobalExceptionInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionInterceptor.class);

    GlobalExceptionInterceptor() {
    }

    public static GlobalExceptionInterceptor newGlobalExceptionInterceptor() {
        return new GlobalExceptionInterceptor();
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata requestHeaders, ServerCallHandler<ReqT, RespT> next) {
        ServerCall.Listener<ReqT> delegate = next.startCall(call, requestHeaders);
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {

            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception e) {
                    // 异常处理项目抛出异常才会走到这里
                    call.close(Status.INTERNAL.withDescription("global info: " + e.getMessage()), new Metadata());
                    log.error("grpc close: ", e);
                }
            }
        };
    }

}