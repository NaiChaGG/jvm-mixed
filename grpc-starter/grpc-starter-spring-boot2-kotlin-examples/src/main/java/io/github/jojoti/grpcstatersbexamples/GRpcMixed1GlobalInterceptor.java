package io.github.jojoti.grpcstatersbexamples;

import io.github.jojoti.grpcstatersb.GRpcScope;
import io.github.jojoti.grpcstatersb.GRpcScopeGlobalInterceptor;

/**
 * 主要的全局拦截器
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcScopeGlobalInterceptor(scope = @GRpcScope(value = GRpcMixed1Service.scopeName))
public @interface GRpcMixed1GlobalInterceptor {
}