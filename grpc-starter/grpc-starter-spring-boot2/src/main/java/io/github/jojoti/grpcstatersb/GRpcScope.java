package io.github.jojoti.grpcstatersb;

import java.lang.annotation.*;

/**
 * 多个 grpc service 作用域
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GRpcScope {

    String value() default "default";

}
