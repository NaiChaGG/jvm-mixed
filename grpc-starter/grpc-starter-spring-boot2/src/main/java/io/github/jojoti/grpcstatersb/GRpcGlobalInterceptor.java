package io.github.jojoti.grpcstatersb;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 全局 grpc 拦截器
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GRpcGlobalInterceptor {

}
