package io.github.jojoti.grpcstartersbcli;

import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.GrpcUtil;
import io.grpc.netty.NettyChannelBuilder;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Wan Steve
 */
class ServiceNameTest {

    @org.junit.jupiter.api.Test
    void getServiceName() {
        NettyChannelBuilder.forTarget("dev_service-admin_1:80");
//        NettyChannelBuilder.forAddress(new InetSocketAddress("dev_service-admin_1", 80));
//
//        var xx = GrpcUtil.authorityFromHostAndPort("dev_service-admin_1", 80);
//        System.out.println(xx);
    }

}