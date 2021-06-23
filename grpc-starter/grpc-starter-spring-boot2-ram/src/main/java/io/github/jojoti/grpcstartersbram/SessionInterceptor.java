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

import com.google.common.collect.ImmutableList;
import io.github.jojoti.grpcstartersb.DynamicScopeFilter;
import io.github.jojoti.grpcstartersb.ScopeServicesEvent;
import io.grpc.*;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * 用户会话拦截器 这个需要依赖于 redis 的实现
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class SessionInterceptor implements ServerInterceptor, DynamicScopeFilter {

    public static final Context.Key<SessionUser> USER_NTS = Context.key("userId");

    // 用户头信息使用这个来获取
    private static final Metadata.Key<String> TOKEN_METADATA_KEY = Metadata.Key.of("x-token", Metadata.ASCII_STRING_MARSHALLER);

    private final Session session;
    private final GRpcSessionProperties gRpcSessionProperties;

    SessionInterceptor(Session session, GRpcSessionProperties gRpcSessionProperties) {
        this.session = session;
        this.gRpcSessionProperties = gRpcSessionProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        final var found = metadata.get(TOKEN_METADATA_KEY);
        // 验证登录会话
        final SessionUser user;
        try {
            user = this.session.verify(found, ImmutableList.<String>builder().build());
        } catch (Exception e) {
            final var error = Status.fromCode(Status.INTERNAL.getCode()).withDescription("info:" + e.getMessage());
            serverCall.close(error, new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        final var context = Context.current().withValue(USER_NTS, user);

        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }

    @Override
    public List<String> getScopes() {
        return this.gRpcSessionProperties.getSession();
    }

    @EventListener
    public void onServicesRegister(ScopeServicesEvent servicesEvent) {
        // fixme 接受启动填充的
    }

}
