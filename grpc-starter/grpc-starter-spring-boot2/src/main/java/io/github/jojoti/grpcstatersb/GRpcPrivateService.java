package io.github.jojoti.grpcstatersb;

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
@GRpcScopeService(scope = @GRpcScope(value = GRpcPrivateService.scopeName))
public @interface GRpcPrivateService {

    String scopeName = "private";

}
