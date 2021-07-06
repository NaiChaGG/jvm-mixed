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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.grpcstartersb.ScopeServerInterceptor;
import io.grpc.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;

/**
 * 用户会话拦截器 这个需要依赖于 redis 的实现
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class SessionInterceptor implements ScopeServerInterceptor, ApplicationContextAware {

    public static final Context.Key<SessionUser> USER_NTS = Context.key("user");

    // 用户头信息使用这个来获取
    private static final Metadata.Key<String> TOKEN_METADATA_KEY = Metadata.Key.of("x-token", Metadata.ASCII_STRING_MARSHALLER);

    private final Session session;
    private final GRpcSessionProperties gRpcSessionProperties;

    private ImmutableMap<MethodDescriptor<?, ?>, ImmutableList<String>> attaches;
    private List<String> globalAttach;

    SessionInterceptor(Session session, GRpcSessionProperties gRpcSessionProperties) {
        this.session = session;
        this.gRpcSessionProperties = gRpcSessionProperties;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        final var found = headers.get(TOKEN_METADATA_KEY);
        // 验证登录会话
        final SessionUser user;
        try {
            user = this.session.verify(found, this.attaches.getOrDefault(call.getMethodDescriptor(), ImmutableList.of()));
        } catch (Exception e) {
            final var error = Status.fromCode(Status.INTERNAL.getCode()).withDescription("info:" + e.getMessage());
            call.close(error, new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        final var context = Context.current().withValue(USER_NTS, user);

        return Contexts.interceptCall(context, call, headers, next);
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
        var found = ServiceDescriptorAnnotations.getAnnotationMaps(servicesEvent, SessionAttach.class, false);
        var builder = ImmutableMap.<MethodDescriptor<?, ?>, ImmutableList<String>>builder();
        for (var entry : found.entrySet()) {
            Preconditions.checkNotNull(entry.getValue().value());
            Preconditions.checkArgument(entry.getValue().value().length > 0, "@SessionAttach value is not allow empty");

            // 校验自定义 attach key 和 全局的 key 不能重复
            // 这里和 全局的 key merge 是 牺牲空间换效率的做法
            final var attach = Lists.<String>newArrayList();
            if (globalAttach != null && globalAttach.size() > 0) {
                attach.addAll(globalAttach);
            }
            for (String s : entry.getValue().value()) {
                if (attach.contains(s)) {
                    throw new IllegalArgumentException("Session attach duplicated key: " + s);
                }
                attach.add(s);
            }

            builder.put(entry.getKey(), Session.checkAttachKey(ImmutableList.copyOf(attach)));
        }
        this.attaches = builder.build();
        // 全局 引用删除 build 完成之后这个已经没啥用了
        this.globalAttach = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final var fundGroup = applicationContext.getBeansWithAnnotation(SessionGlobalAttach.class);
        if (fundGroup.size() > 0) {
            final var foundValues = Lists.<String>newArrayList();
            for (Object value : fundGroup.values()) {
                var found = AnnotationUtils.findAnnotation(value.getClass(), SessionGlobalAttach.class);
                if (found != null) {
                    Preconditions.checkArgument(found.value().length > 0, "@SessionGlobalAttach value is not allow empty");
                    for (String s : found.value()) {
                        if (foundValues.contains(s)) {
                            throw new IllegalArgumentException("Session global attach duplicated key: " + s);
                        }
                        foundValues.add(s);
                    }
                }
            }
            Session.checkAttachKey(ImmutableList.copyOf(foundValues));
            this.globalAttach = foundValues;
        }
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
