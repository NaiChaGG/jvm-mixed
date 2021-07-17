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
import io.github.jojoti.grpcstartersb.GRpcScope;
import io.grpc.*;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * https://stackoverflow.com/questions/68164315/access-message-request-in-first-grpc-interceptor-before-headers-in-second-grpc-i
 * <p>
 * 此接口是对拦截器行为的扩展
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 * @see io.grpc.ServerInterceptor
 */
public interface RAMAccessInterceptor {

    static <ReqT, RespT> ServerCall.Listener<ReqT> newDefaultPermissionDenied(ServerCall<ReqT, RespT> call) {
        final var error = Status.fromCode(Status.PERMISSION_DENIED.getCode()).withDescription("Auth failed, please check access");
        call.close(error, new Metadata());
        return new ServerCall.Listener<>() {
        };
    }

    // 启动注册 ram 列表权限
    // 这个可能会执行多次
    default void onRegister(GRpcScope gRpcScope, ImmutableList<RegisterRAM> allServices) {

    }

    /**
     * 没有配置 @RAMAllowAnonymous 都需要校验会话
     * 校验会话比较特殊 写在这里
     *
     * @param gRpcScope 标注的注解属于哪个模块
     * @param ram       注解标记的属性
     */
    default <ReqT, RespT> boolean checkSession(GRpcScope gRpcScope, RegisterRAMItem ram,
                                               ServerCall<ReqT, RespT> call,
                                               Metadata headers,
                                               ServerCallHandler<ReqT, RespT> next) {
        // 后续可以根据 不同的 scope 选择不同的实现
        // 默认使用 内置 session 实现
        return !SessionInterceptor.USER_NTS.get().isAnonymous();
    }

    /**
     * @param gRpcScope
     * @param ram
     * @param <ReqT>
     * @param <RespT>
     * @return null 表示走默认失败流程 not null 表示成功
     */
    <ReqT, RespT> ServerCall.Listener<ReqT> checkAccess(GRpcScope gRpcScope, RegisterRAMItem ram,
                                                        ServerCall<ReqT, RespT> call,
                                                        Metadata headers,
                                                        ServerCallHandler<ReqT, RespT> next);

    final class RegisterRAM {

        private final BindableService serviceObject;
        private final ImmutableList<RegisterRAMItem> methods;

        RegisterRAM(BindableService serviceObject, ImmutableList<RegisterRAMItem> methods) {
            this.serviceObject = serviceObject;
            this.methods = methods;
        }

        public Object getServiceObject() {
            return serviceObject;
        }

        public ImmutableList<RegisterRAMItem> getMethods() {
            return methods;
        }

    }

    final class RegisterRAMItem {
        private final MethodDescriptor<?, ?> methodDesc;
        private final Method method;

        RegisterRAMItem(MethodDescriptor<?, ?> methodDesc, Method method) {
            this.methodDesc = methodDesc;
            this.method = method;
        }

        /**
         * 找不到报错
         *
         * @param extAnnotationClass
         * @param <T>
         * @return
         */
        public <T extends Annotation> T getRAMExtension(Class<T> extAnnotationClass) {
            return containsRAMExtension(extAnnotationClass, true);
        }

        /**
         * 找不到 返回 null
         *
         * @param extAnnotationClass
         * @param <T>
         * @return
         */
        public <T extends Annotation> T findRAMExtension(Class<T> extAnnotationClass) {
            return containsRAMExtension(extAnnotationClass, false);
        }

        private <T extends Annotation> T containsRAMExtension(Class<T> extAnnotationClass, boolean forced) {
            final var found = AnnotationUtils.findAnnotation(method, extAnnotationClass);
            if (found == null) {
                if (forced) {
                    throw new IllegalArgumentException("Method: " + method + "@" + extAnnotationClass + " must be used.");
                }
                return null;
            }
            final var foundRAM = AnnotationUtils.findAnnotation(found.getClass(), RAM.class);
            Preconditions.checkNotNull(foundRAM, "Ram extension must be use " + RAM.class + ", ref: " + RAMAllowAnonymous.class);
            return found;
        }

        public boolean isAllowAnonymous() {
            return findRAMExtension(RAMAllowAnonymous.class) != null;
        }

        public RAMDeclare getRAMDeclare() {
            return getRAMExtension(RAMDeclare.class);
        }

        public MethodDescriptor<?, ?> getMethodDesc() {
            return methodDesc;
        }

        public Method getMethod() {
            return method;
        }
    }


}
