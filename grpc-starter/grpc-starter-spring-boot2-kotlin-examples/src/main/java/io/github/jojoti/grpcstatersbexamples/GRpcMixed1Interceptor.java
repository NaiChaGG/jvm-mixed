package io.github.jojoti.grpcstatersbexamples;

import io.github.jojoti.grpcstatersb.GRpcScope;
import io.github.jojoti.grpcstatersb.GRpcScopeServiceInterceptor;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcScopeServiceInterceptor(scope = @GRpcScope(value = GRpcMixed1Service.scopeName))
public @interface GRpcMixed1Interceptor {
}
