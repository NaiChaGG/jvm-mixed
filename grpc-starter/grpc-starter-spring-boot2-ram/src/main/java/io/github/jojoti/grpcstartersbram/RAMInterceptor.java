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
import com.google.common.collect.ImmutableMap;
import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.grpcstartersb.ScopeServerInterceptor;
import io.grpc.*;

import java.util.List;

/**
 * 访问控制和用户会话拦截器
 * 拦截器需要 使用者 自行添加到 grpc server 里面去，多server无法知道用户注入哪里
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class RAMInterceptor implements ScopeServerInterceptor {

    private final RAMAccess ramAccess;
    private final GRpcRAMProperties gRpcRAMProperties;

    private ImmutableList<MethodDescriptor<?, ?>> allowAnonymous;
    private ImmutableMap<MethodDescriptor<?, ?>, RAM> rams;
    private GRpcScope currentGRpcScope;

    RAMInterceptor(RAMAccess ramAccess, GRpcRAMProperties gRpcRAMProperties) {
        this.ramAccess = ramAccess;
        this.gRpcRAMProperties = gRpcRAMProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata,
                                                                 ServerCallHandler<ReqT, RespT> serverCallHandler) {
        // 允许匿名访问的接口不校验 权限
        if (this.allowAnonymous.contains(serverCall.getMethodDescriptor())) {
            return serverCallHandler.startCall(serverCall, metadata);
        }

        final var foundRam = rams.get(serverCall.getMethodDescriptor());
        if (foundRam != null) {
            try {
                // 判断用户是否登陆
                var isLogin = this.ramAccess.checkSession(this.currentGRpcScope, serverCall.getMethodDescriptor(), foundRam, metadata);
                if (isLogin) {
                    // 权限不足
                    final var error = Status.fromCode(Status.UNAUTHENTICATED.getCode()).withDescription("Auth failed, please check session");
                    serverCall.close(error, new Metadata());
                    return new ServerCall.Listener<>() {
                    };
                }
                // 可以从 metadata 获取 ip 啥的
                var rs = this.ramAccess.access(this.currentGRpcScope, serverCall.getMethodDescriptor(), foundRam, metadata);
                if (rs) {
                    return serverCallHandler.startCall(serverCall, metadata);
                }
            } catch (Exception e) {
                final var error = Status.fromCode(Status.INTERNAL.getCode()).withDescription("info:" + e.getMessage());
                serverCall.close(error, new Metadata());
                return new ServerCall.Listener<>() {
                };
            }
        }

        // 权限不足
        final var error = Status.fromCode(Status.PERMISSION_DENIED.getCode()).withDescription("Auth failed, please check access");
        serverCall.close(error, new Metadata());
        return new ServerCall.Listener<>() {
        };
    }

    @Override
    public List<String> getScopes() {
        return this.gRpcRAMProperties.enableScopeNames();
    }

    @Override
    public void aware(GRpcScope currentGRpcScope, ImmutableList<BindableService> servicesEvent) {
        this.currentGRpcScope = currentGRpcScope;

        this.addAllowAnonymous(servicesEvent);

        var found = ServiceDescriptorAnnotations.getAnnotationMaps(servicesEvent, RAM.class, true);
        var builder = ImmutableMap.<MethodDescriptor<?, ?>, RAM>builder();
        for (var entry : found.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }
        this.rams = builder.build();
    }

    private void addAllowAnonymous(List<BindableService> servicesEvent) {
        var foundAllowAnonymous = ServiceDescriptorAnnotations.getAnnotationMaps(servicesEvent, RAMAllowAnonymous.class, false);
        var builder = ImmutableList.<MethodDescriptor<?, ?>>builder();
        for (var entry : foundAllowAnonymous.entrySet()) {
            builder.add(entry.getKey());
        }
        this.allowAnonymous = builder.build();
    }

    @Override
    public ScopeServerInterceptor cloneThis() {
        try {
            return (ScopeServerInterceptor) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}