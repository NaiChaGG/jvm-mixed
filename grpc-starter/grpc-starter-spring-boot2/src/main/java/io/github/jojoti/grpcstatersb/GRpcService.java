package io.github.jojoti.grpcstatersb;

import io.grpc.ServerInterceptor;
import org.springframework.stereotype.Component;

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
@Component
public @interface GRpcService {

    Class<? extends ServerInterceptor>[] interceptors() default {};

    boolean applyGlobalInterceptors() default true;

}
