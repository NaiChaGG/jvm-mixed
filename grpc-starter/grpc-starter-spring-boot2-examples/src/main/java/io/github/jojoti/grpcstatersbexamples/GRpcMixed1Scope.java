package io.github.jojoti.grpcstatersbexamples;

import io.github.jojoti.grpcstatersb.GRpcScope;

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
@GRpcScope(value = GRpcMixed1Scope.scopeName)
public @interface GRpcMixed1Scope {

    // 这里需要与 配置里面的 scopeName 一致
    String scopeName = "mixed1";

}
