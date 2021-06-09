package io.github.jojoti.grpcstatersb;

/**
 * 主要的全局拦截器
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcScopeGlobalInterceptor(scope = @GRpcScope(value = GRpcPrimaryService.scopeName))
public @interface GRpcPrimaryGlobalInterceptor {
}