package io.github.jojoti.grpcstatersbexamples;

import io.grpc.ServerInterceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *
 * 主要的 grpc server
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@GRpcMixed1Scope
public @interface GRpcMixed1Service {

    Class<? extends ServerInterceptor>[] interceptors() default {};

    boolean applyGlobalInterceptors() default true;

}
