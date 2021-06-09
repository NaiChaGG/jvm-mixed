package io.github.jojoti.grpcstatersb;

import io.grpc.ServerInterceptor;

import java.lang.annotation.*;

/**
 * 带作用域的 grpc starter
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GRpcServiceInterceptors {

    Class<? extends ServerInterceptor>[] interceptors();

    // 启用或者关闭全局拦截器
    boolean applyGlobalInterceptors() default true;

}
