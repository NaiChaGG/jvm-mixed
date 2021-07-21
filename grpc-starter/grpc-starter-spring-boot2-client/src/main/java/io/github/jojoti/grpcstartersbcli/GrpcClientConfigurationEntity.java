package io.github.jojoti.grpcstartersbcli;

import io.grpc.netty.NettyChannelBuilder;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public final class GrpcClientConfigurationEntity {

    private final String serviceName;
    private final NettyChannelBuilder nettyChannelBuilder;

     GrpcClientConfigurationEntity(String serviceName, NettyChannelBuilder nettyChannelBuilder) {
        this.serviceName = serviceName;
        this.nettyChannelBuilder = nettyChannelBuilder;
    }

    public String getServiceName() {
        return serviceName;
    }

    public NettyChannelBuilder getNettyChannelBuilder() {
        return nettyChannelBuilder;
    }

}
