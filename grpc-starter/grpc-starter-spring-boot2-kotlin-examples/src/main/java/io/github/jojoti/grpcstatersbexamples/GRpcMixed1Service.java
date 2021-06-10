package io.github.jojoti.grpcstatersbexamples;

import io.github.jojoti.grpcstatersb.GRpcScope;
import io.github.jojoti.grpcstatersb.GRpcScopeService;

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
@GRpcScopeService(scope = @GRpcScope(value = GRpcMixed1Service.scopeName))
public @interface GRpcMixed1Service {
    String scopeName = "mixed1";

}
