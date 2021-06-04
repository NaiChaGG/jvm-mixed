package io.github.jojoti.grpcstatersb;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 *
 * 私密的 grpc server
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@GRpcScope(GRpcPrivateScope.scopeName)
public @interface GRpcPrivateScope {

    String scopeName = "private";

}
