package io.github.jojoti.grpcstartersbcli;

import io.grpc.netty.NettyChannelBuilder;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface GrpcClientFilter {

    void onFilter(String serviceName, NettyChannelBuilder nettyChannelBuilder);

}
