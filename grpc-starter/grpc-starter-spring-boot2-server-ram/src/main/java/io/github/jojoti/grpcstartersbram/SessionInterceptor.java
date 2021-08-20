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
import com.google.common.collect.Maps;
import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.grpcstartersb.ScopeServerInterceptor;
import io.github.jojoti.utilguavaext.GuavaCollects;
import io.grpc.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 用户会话拦截器 这个需要依赖于 redis 的实现
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class SessionInterceptor implements ScopeServerInterceptor, ApplicationContextAware {

    public static final Context.Key<SessionUser> USER_NTS = Context.key("user");

    public static final Metadata.Key<String> TOKEN_METADATA_KEY = Metadata.Key.of("x-token", Metadata.ASCII_STRING_MARSHALLER);

    @Autowired
    Session session;

    @Autowired
    GRpcSessionProperties gRpcSessionProperties;

    private ImmutableMap<MethodDescriptor<?, ?>, ImmutableList<String>> attaches;

    // 临时字段
    private ImmutableList<String> globalAttach;
    private ApplicationContext applicationContext;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        // 验证登录会话
        final SessionUser user;
        try {
            final var headerValue = this.getHeaderToken(headers);
            if (Strings.isNullOrEmpty(headerValue)) {
                final var error = Status.fromCode(Status.INTERNAL.getCode()).withDescription("session valid header is empty");
                call.close(error, new Metadata());
                return new ServerCall.Listener<>() {
                };
            }
            user = this.session.verify(headerValue, this.attaches.getOrDefault(call.getMethodDescriptor(), ImmutableList.of()));
        } catch (Exception e) {
            final var error = Status.fromCode(Status.INTERNAL.getCode()).withDescription("session valid error info:" + e.getMessage());
            call.close(error, new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        final var context = Context.current().withValue(USER_NTS, user);
        return Contexts.interceptCall(context, call, headers, next);
    }

    /**
     * 允许 用户 自定义扩展 header 头的处理
     *
     * @param headers
     * @return
     */
    protected String getHeaderToken(Metadata headers) {
        return headers.get(TOKEN_METADATA_KEY);
    }

    @Override
    public List<String> getScopes() {
        // fixme session 暂不支持多个 scope 配置 这样又会涉及到 取 session redis 多数据源问题
        // 暂未想到应用场景
//        Preconditions.checkArgument(this.gRpcSessionProperties.enableScopeNames().size() == 1, "Session is not support multi.");
        return this.gRpcSessionProperties.enableScopeNames();
    }

    @Override
    public void aware(GRpcScope currentGRpcScope, ImmutableList<BindableService> servicesEvent) {
        this.addGlobalScopeAttach(currentGRpcScope);

        final var found = ServiceDescriptorAnnotations.getAnnotationMapsV2(servicesEvent, SessionAttach.class, false);

        final var newAttaches = Maps.<MethodDescriptor<?, ?>, ImmutableList<String>>newHashMap();

        for (var sessionAttachServiceMethods : found) {
            for (var method : sessionAttachServiceMethods.methods) {
                Preconditions.checkNotNull(method.foundAnnotation.keys());
                Preconditions.checkArgument(method.foundAnnotation.keys().length > 0, "@SessionAttach value is not allow empty");
                final var attach = Lists.<String>newArrayList();
                for (String s : method.foundAnnotation.keys()) {
                    if (attach.contains(s)) {
                        throw new IllegalArgumentException("Session attach duplicated key: " + s);
                    }
                    attach.add(s);
                }
                newAttaches.put(method.methodDescriptor, ImmutableList.copyOf(attach));
            }
        }

        // 存在全局拦截器
        // 追加全局拦截器
        final var builder = ImmutableMap.<MethodDescriptor<?, ?>, ImmutableList<String>>builder();
        if (this.globalAttach.size() > 0) {
            for (BindableService bindableService : servicesEvent) {
                for (ServerMethodDefinition<?, ?> method : bindableService.bindService().getMethods()) {
                    final var foundScope = newAttaches.get(method.getMethodDescriptor());
                    // 校验自定义 attach key 和 全局的 key 不能重复
                    // 这里和 全局的 key merge 是 牺牲空间换效率的做法
                    if (foundScope != null) {
                        builder.put(method.getMethodDescriptor(), ImmutableList.<String>builder().addAll(foundScope).addAll(this.globalAttach).build());
                    } else {
                        builder.put(method.getMethodDescriptor(), ImmutableList.<String>builder().addAll(this.globalAttach).build());
                    }
                }
            }
        } else {
            for (var entry : newAttaches.entrySet()) {
                builder.put(entry.getKey(), entry.getValue());
            }
        }

        this.attaches = builder.build();

        // 全局 引用删除 build 完成之后这个已经没啥用了
        this.globalAttach = null;
    }

    private void addGlobalScopeAttach(GRpcScope currentGRpcScope) {
        final var fundGroup = applicationContext.getBeansWithAnnotation(SessionGlobalAttach.class);
        if (fundGroup.size() > 0) {
            final var foundValues = Lists.<String>newArrayList();
            for (Object value : fundGroup.values()) {
                var found = AnnotationUtils.findAnnotation(value.getClass(), SessionGlobalAttach.class);
                if (found != null) {
                    // scope name 不唯一
                    var groups = GuavaCollects.listGroupMultimap(Arrays.asList(found.scopes()), SessionGlobalAttach.ScopeAttach::scopeName);
                    Preconditions.checkArgument(groups.size() == found.scopes().length, "Scope name duplicated");
                    for (SessionGlobalAttach.ScopeAttach scope : found.scopes()) {
                        // 需要和当前的 scope 匹配 不匹配直接忽略
                        if (!currentGRpcScope.value().equals(scope.scopeName())) {
                            continue;
                        }
                        Preconditions.checkArgument(scope.value().length > 0, "@SessionGlobalAttach " + scope.scopeName() + " value is not allow empty");
                        for (String s : scope.value()) {
                            if (foundValues.contains(s)) {
                                throw new IllegalArgumentException("Session scope " + scope.scopeName() + " global attach duplicated key: " + s);
                            }
                            foundValues.add(s);
                        }
                    }
                }
            }
            this.globalAttach = ImmutableList.copyOf(foundValues);
        } else {
            this.globalAttach = ImmutableList.of();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
