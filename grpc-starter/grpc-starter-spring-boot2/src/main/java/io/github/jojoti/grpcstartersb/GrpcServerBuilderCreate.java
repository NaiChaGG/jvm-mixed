package io.github.jojoti.grpcstartersb;

import io.grpc.ServerBuilder;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GrpcServerBuilderCreate {

    private final GRpcScope gRpcScope;
    private final ServerBuilder<?> serverBuilder;

    GrpcServerBuilderCreate(GRpcScope gRpcScope, ServerBuilder<?> serverBuilder) {
        this.gRpcScope = gRpcScope;
        this.serverBuilder = serverBuilder;
    }

    public GRpcScope getgRpcScope() {
        return gRpcScope;
    }

    public ServerBuilder<?> getServerBuilder() {
        return serverBuilder;
    }

}
