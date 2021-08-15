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

package io.github.jojoti.grpcstartersbram;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.grpcstartersb.ScopeServerInterceptor;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

/**
 * 访问控制和用户会话拦截器
 * 拦截器需要 使用者 自行添加到 grpc server 里面去，多server无法知道用户注入哪里
 * https://stackoverflow.com/questions/68164315/access-message-request-in-first-grpc-interceptor-before-headers-in-second-grpc-i
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class RAMInterceptor implements ScopeServerInterceptor, CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RAMInterceptor.class);

    private final RAMAccessInterceptor ramAccessInterceptor;
    private final GRpcRAMProperties gRpcRAMProperties;

    private ImmutableMap<MethodDescriptor<?, ?>, RAMAccessInterceptor.RegisterRAMItem> rams;
    private GRpcScope currentGRpcScope;
    private ImmutableList<RAMAccessInterceptor.RegisterRAM> allServices;

    RAMInterceptor(RAMAccessInterceptor ramAccessInterceptor, GRpcRAMProperties gRpcRAMProperties) {
        this.ramAccessInterceptor = ramAccessInterceptor;
        this.gRpcRAMProperties = gRpcRAMProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        final var foundRam = rams.get(call.getMethodDescriptor());
        if (log.isDebugEnabled()) {
            log.debug("ram , fullmethodname {}", call.getMethodDescriptor().getFullMethodName());
        }
        if (foundRam != null) {
            try {
                // 允许匿名直接跳过校验
                if (foundRam.isAllowAnonymous()) {
                    return next.startCall(call, headers);
                }
                // 判断用户是否登陆
                var isLogin = this.ramAccessInterceptor.checkSession(this.currentGRpcScope, foundRam, call, headers, next);
                if (!isLogin) {
                    // 权限不足
                    final var error = Status.fromCode(Status.UNAUTHENTICATED.getCode()).withDescription("Auth failed, please check session");
                    call.close(error, new Metadata());
                    return new ServerCall.Listener<>() {
                    };
                }
                // 可以从 metadata 获取 ip 啥的
                var rs = this.ramAccessInterceptor.checkAccess(this.currentGRpcScope, foundRam, call, headers, next);
                if (rs != null) {
                    return rs;
                }
            } catch (Exception e) {
                final var error = Status.fromCode(Status.INTERNAL.getCode()).withDescription("info:" + e.getMessage());
                call.close(error, new Metadata());
                return new ServerCall.Listener<>() {
                };
            }
        }

        return RAMAccessInterceptor.newDefaultPermissionDenied(call);
    }

    @Override
    public List<String> getScopes() {
        return this.gRpcRAMProperties.enableScopeNames();
    }

    @Override
    public void aware(GRpcScope currentGRpcScope, ImmutableList<BindableService> servicesEvent) {
        this.currentGRpcScope = currentGRpcScope;

        // 启用 ram 必须use @Ram 注解
        final var services = ServiceDescriptorAnnotations.getAnnotationMapsV2(servicesEvent, RAM.class, true);
        final var awareBuilder = ImmutableList.<RAMAccessInterceptor.RegisterRAM>builder();

        for (var service : services) {
            final var methods = ImmutableList.<RAMAccessInterceptor.RegisterRAMItem>builder();
            for (var method : service.methods) {
                methods.add(new RAMAccessInterceptor.RegisterRAMItem(method.methodDescriptor, method.method));
            }
            awareBuilder.add(new RAMAccessInterceptor.RegisterRAM(service.object, methods.build()));
        }

        final var awareServices = awareBuilder.build();

        final var ramsBuilder = ImmutableMap.<MethodDescriptor<?, ?>, RAMAccessInterceptor.RegisterRAMItem>builder();

        for (RAMAccessInterceptor.RegisterRAM awareService : awareServices) {
            for (RAMAccessInterceptor.RegisterRAMItem method : awareService.getMethods()) {
                ramsBuilder.put(method.getMethodDesc(), method);
            }
        }

        this.rams = ramsBuilder.build();
        this.allServices = awareServices;
    }

    @Override
    public ScopeServerInterceptor cloneThis() {
        try {
            return (ScopeServerInterceptor) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        // 服务启动后再执行初始化操作
        this.ramAccessInterceptor.runAfterRegister(currentGRpcScope, allServices, args);
        this.allServices = null;
    }

}