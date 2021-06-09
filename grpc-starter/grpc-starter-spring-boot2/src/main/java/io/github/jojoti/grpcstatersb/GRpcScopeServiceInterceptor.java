package io.github.jojoti.grpcstatersb;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 带作用域的 grpc interceptor 如果被引用的地方不一致则会直接报错
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GRpcScopeServiceInterceptor {
    GRpcScope scope() default @GRpcScope;
}
