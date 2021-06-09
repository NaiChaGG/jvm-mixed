package io.github.jojoti.grpcstatersb;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcScopeServiceInterceptor(scope = @GRpcScope(value = GRpcPrivateService.scopeName))
public @interface GRpcPrivateInterceptor {
}
