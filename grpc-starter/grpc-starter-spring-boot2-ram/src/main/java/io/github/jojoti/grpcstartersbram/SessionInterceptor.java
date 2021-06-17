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

package io.github.jojoti.grpcstartersbram;

import io.grpc.*;

/**
 * 用户会话拦截器 这个需要依赖于 redis 的实现
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public abstract class SessionInterceptor implements ServerInterceptor, SessionCreator {

    public static final Context.Key<SessionUser> USER_ID_NTS = Context.key("userId");

    // 用户头信息使用这个来获取
    private static final Metadata.Key<String> TOKEN_METADATA_KEY = Metadata.Key.of("x-token", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        final var found = metadata.get(TOKEN_METADATA_KEY);
        if (found == null) {
            serverCall.close(Status.UNAUTHENTICATED, new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        // 验证登录会话
        final var user = this.valid(found);

        final var context = Context.current().withValue(USER_ID_NTS, user);

        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }

}
