package io.github.jojoti.grpcstartersb;

import io.grpc.ServerInterceptor;
import io.grpc.ServiceDescriptor;

import java.util.List;

/**
 * @author Wang Yue
 */
public interface ScopeServerInterceptor extends ServerInterceptor {

    // 当前 scope 下 所有的 定义
    void onServicesRegister(GRpcScope scope, List<ServiceDescriptor> servicesEvent);

}
