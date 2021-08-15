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

package io.github.jojoti.grpcstartersbexamples;

import com.google.common.base.Strings;
import io.grpc.*;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcMixed1Interceptor
public class App1Interceptor implements ServerInterceptor {

    public static final Context.Key<Long> USER_ID_NTS = Context.key("userId");
    private static final Metadata.Key<String> USER_ID_METADATA_KEY = Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER);

    public App1Interceptor() {
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var get = metadata.get(USER_ID_METADATA_KEY);
        if (Strings.isNullOrEmpty(get)) {
            serverCall.close(Status.UNAUTHENTICATED, new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        var userId = Long.parseLong(get);

        final var context = Context.current().withValue(USER_ID_NTS, userId);

        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }

}