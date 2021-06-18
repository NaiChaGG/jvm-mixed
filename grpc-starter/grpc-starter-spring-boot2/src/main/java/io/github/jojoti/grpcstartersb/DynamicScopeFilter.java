package io.github.jojoti.grpcstartersb;

/*
 * 此拦截器用于动态拦截某些 scope 因为某些场景不能使用 注解标注，比如，全局拦截器只添加到某个特定 scope 下
 * 如: @GRpcGlobalInterceptor 要添加 到动态特定的 scope 下，那么目前的 注解是无法满足需求的
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface DynamicScopeFilter {

    GRpcScope getScope();

}
