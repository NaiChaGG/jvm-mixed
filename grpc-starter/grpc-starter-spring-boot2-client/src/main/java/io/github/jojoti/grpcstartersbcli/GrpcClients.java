/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jojoti.grpcstartersbcli;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.jojoti.grpcstartersbcli.autoconfigure.GRpcClientProperties;
import io.github.jojoti.utildaemonthreads.DaemonThreads;
import io.github.jojoti.utilguavaext.GetAddress;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.NettyChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GrpcClients implements SmartLifecycle, GrpcClientContext {

    private static final Logger log = LoggerFactory.getLogger(GrpcClients.class);

    private final GRpcClientProperties gRpcClientProperties;
    private final GrpcClientFilter grpcClientFilter;

    private volatile DaemonThreads daemonThreads;
    private ImmutableMap<Map.Entry<String, GRpcClientProperties.ClientItem>, ManagedChannel> channels;

    public GrpcClients(GRpcClientProperties gRpcClientProperties, GrpcClientFilter grpcClientFilter) {
        this.gRpcClientProperties = gRpcClientProperties;
        this.grpcClientFilter = grpcClientFilter;
    }

    @Override
    public void start() {
        Preconditions.checkArgument(this.gRpcClientProperties.getClients() != null && this.gRpcClientProperties.getClients().size() > 0, "Servers is not allow empty");
        log.info("Starting gRPC client ...");

        final var clients = Maps.<Map.Entry<String, GRpcClientProperties.ClientItem>, ManagedChannelBuilder<?>>newHashMap();

        for (var client : this.gRpcClientProperties.getClients().entrySet()) {
            // fixme 目前只支持 vip 网络这种模式发现
            // 所有直接写死
            final var vip = this.gRpcClientProperties.getDiscovery().getVip();
            Preconditions.checkNotNull(vip);
            final var vipAddress = vip.get(client.getKey());
            Preconditions.checkArgument(!Strings.isNullOrEmpty(vipAddress), "ServiceName " + client.getKey() + " vip address not found");

            final var builder = NettyChannelBuilder.forAddress(GetAddress.getSocketAddress(vipAddress));
            // 通知 自定义 配置
            this.grpcClientFilter.onFilter(client.getKey(), builder);
            clients.put(client, builder);
        }

        final var daemon = DaemonThreads.newDaemonThreads(this.gRpcClientProperties.getClients().size(),
                "Multi grpc client awaiter", (handler, e) -> {
                    log.error("E: {}", handler, e);
                });

        final var clientChannels = ImmutableMap.<Map.Entry<String, GRpcClientProperties.ClientItem>, ManagedChannel>builder();

        for (final var entry : clients.entrySet()) {
            try {
                daemon.startThreads(entry.getKey().getKey(), () -> {
                    final var channel = entry.getValue().build();
                    clientChannels.put(entry.getKey(), channel);
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        this.channels = clientChannels.build();
        this.daemonThreads = daemon;
    }

    @Override
    public void stop() {
        if (this.channels != null) {
            for (var value : this.channels.entrySet()) {
                this.daemonThreads.downThreads(value.getKey().getKey(), () -> {
                    log.info("gRPC {} client stop", value.getKey());
                    value.getValue().shutdown();
                    value.getValue().awaitTermination(value.getKey().getValue().getShutdownGracefullyMills(), TimeUnit.MILLISECONDS);
                });
            }
            this.channels = null;
            this.daemonThreads = null;
        }
    }

    @Override
    public boolean isRunning() {
        return this.daemonThreads != null && this.daemonThreads.isHealth();
    }

    @Override
    public <T extends Enum<T>> Channel getChannel(ServiceName<T> serviceName) {
        for (var entry : channels.entrySet()) {
            if (entry.getKey().getKey().equals(serviceName.getServiceName())) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("ServiceName " + serviceName.getServiceName() + " not fund");
    }

}
