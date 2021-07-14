package io.github.jojoti.grpcstartersbcli;

import io.grpc.Channel;

/**
 * 从 grpc client 容器里面取出对应的 grpc client
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface GrpcClientContext {

    /**
     * 获取 channel 来创建
     *
     * @param serviceName
     * @param <T>
     * @return
     */
    <T extends Enum<T>> Channel getChannel(ServiceName<T> serviceName);

}
