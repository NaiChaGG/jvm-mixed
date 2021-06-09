package io.github.jojoti.grpcstatersb;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcScopeGlobalInterceptor(scope = @GRpcScope(value = GRpcPrivateService.scopeName))
public @interface GRpcPrivateGlobalInterceptor {
}
