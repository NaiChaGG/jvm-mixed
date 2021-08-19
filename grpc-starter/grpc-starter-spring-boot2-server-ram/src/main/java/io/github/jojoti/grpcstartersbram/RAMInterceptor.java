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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.grpcstartersb.ScopeServerInterceptor;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.Comparator;
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

    private List<RAMAccessInterceptorCondition> ramAccessInterceptorCondition;
    private final GRpcRAMProperties gRpcRAMProperties;

    private ImmutableMap<MethodDescriptor<?, ?>, RAMAccessInterceptor.RegisterRAMItem> rams;
    private ImmutableList<RAMAccessInterceptor.RegisterRAM> allServices;
    private ImmutableList<RAMAccessInterceptor> ramAccessInterceptor;

    RAMInterceptor(List<RAMAccessInterceptorCondition> ramAccessInterceptorCondition, GRpcRAMProperties gRpcRAMProperties) {
        final var arrayCopy = Lists.newArrayList(ramAccessInterceptorCondition);
        // 也可以通过 spring order 注解来排序
        arrayCopy.sort(Comparator.comparingInt(RAMAccessInterceptorCondition::ordered));
        this.ramAccessInterceptorCondition = arrayCopy;
        this.gRpcRAMProperties = gRpcRAMProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        final var foundRam = rams.get(call.getMethodDescriptor());
        if (log.isDebugEnabled()) {
            log.debug("ram , fullMethodName {}", call.getMethodDescriptor().getFullMethodName());
        }
        if (foundRam != null) {
            try {
                // 允许匿名直接跳过校验
                if (foundRam.isAllowAnonymous()) {
                    return next.startCall(call, headers);
                }
                ServerCall.Listener<ReqT> success = null;
                for (RAMAccessInterceptor accessInterceptor : this.ramAccessInterceptor) {
                    // 可以从 metadata 获取 ip 啥的
                    var rs = accessInterceptor.checkNext(foundRam, call, headers, next);
                    if (rs.isFailed()) {
                        Preconditions.checkNotNull(rs.getData());
                        return rs.getData();
                    }
                    // 成功的 listener 暂时只支持返回一个
                    if (rs.getData() != null && success != null) {
                        throw new IllegalArgumentException("Success Listener can only.");
                    }
                    success = rs.getData();
                }
                if (success != null) {
                    return success;
                }
                return next.startCall(call, headers);
            } catch (Exception e) {
                final var error = Status.fromCode(Status.INTERNAL.getCode()).withDescription("info:" + e.getMessage());
                call.close(error, new Metadata());
                return new ServerCall.Listener<>() {
                };
            }
        }

        final var error = Status.fromCode(Status.PERMISSION_DENIED.getCode()).withDescription("Auth failed, please check access");
        call.close(error, new Metadata());
        return new ServerCall.Listener<>() {
        };
    }

    @Override
    public List<String> getScopes() {
        return this.gRpcRAMProperties.enableScopeNames();
    }

    @Override
    public void aware(GRpcScope currentGRpcScope, ImmutableList<BindableService> servicesEvent) {
        final var found = (int) this.ramAccessInterceptorCondition.stream().filter(c -> c.matches(currentGRpcScope)).count();
        Preconditions.checkArgument(found > 0, Strings.lenientFormat("Scope name %s ram access interceptor not found", currentGRpcScope));

        final var accesses = ImmutableList.<RAMAccessInterceptor>builder();

        // 必须要找到 对应的
        for (RAMAccessInterceptorCondition accessInterceptorCondition : this.ramAccessInterceptorCondition) {
            if (accessInterceptorCondition.matches(currentGRpcScope)) {
                accesses.add(accessInterceptorCondition.newRAMAccessInterceptor());
                break;
            }
        }

        this.ramAccessInterceptor = accesses.build();

        // 释放引用
        this.ramAccessInterceptorCondition = null;

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
    public void run(String... args) {
        // 服务启动后再执行初始化操作
        this.ramAccessInterceptor.forEach(c -> c.onStartRegister(allServices));
        this.allServices = null;
    }

}