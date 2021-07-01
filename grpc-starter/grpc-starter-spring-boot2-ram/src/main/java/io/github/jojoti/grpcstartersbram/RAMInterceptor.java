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

import io.github.jojoti.grpcstartersb.DynamicScopeFilter;
import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.grpcstartersb.ScopeServerInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServiceDescriptor;

import java.util.List;

/**
 * 访问控制和用户会话拦截器
 * 拦截器需要 使用者 自行添加到 grpc server 里面去，多server无法知道用户注入哪里
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class RAMInterceptor implements ScopeServerInterceptor, DynamicScopeFilter {

    private final RAMAccess ramAccess;
    private final GRpcRAMProperties gRpcRAMProperties;

    RAMInterceptor(RAMAccess ramAccess, GRpcRAMProperties gRpcRAMProperties) {
        this.ramAccess = ramAccess;
        this.gRpcRAMProperties = gRpcRAMProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        return null;
    }

    @Override
    public List<String> getScopes() {
        return this.gRpcRAMProperties.enableScopeNames();
    }

    @Override
    public void onServicesRegister(GRpcScope scope, List<ServiceDescriptor> servicesEvent) {

    }

}