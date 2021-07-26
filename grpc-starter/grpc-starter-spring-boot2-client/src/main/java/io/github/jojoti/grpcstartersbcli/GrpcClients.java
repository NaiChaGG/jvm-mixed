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
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.NettyChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * https://github.com/grpc/grpc-java/blob/master/core/src/test/java/io/grpc/internal/DnsNameResolverProviderTest.java#L61
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GrpcClients implements SmartLifecycle, GrpcClientContext, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(GrpcClients.class);

    private final GRpcClientProperties gRpcClientProperties;

    private volatile DaemonThreads daemonThreads;
    private ImmutableMap<Map.Entry<String, GRpcClientProperties.ClientItem>, ManagedChannel> channels;
    private ApplicationContext applicationContext;

    public GrpcClients(GRpcClientProperties gRpcClientProperties) {
        this.gRpcClientProperties = gRpcClientProperties;
    }

    @Override
    public void start() {
        Preconditions.checkArgument(
                this.gRpcClientProperties.getClients() != null
                        && this.gRpcClientProperties.getClients().size() > 0, "Grpc clients is not allow empty");
        log.info("Starting gRPC client ...");

        //        if (this.gRpcClientProperties.getNr() != null) {
//            DnsNameResolverProvider dns = null;
//            SynchronizationContext syncContext = null;
//            // https://github.com/grpc/grpc-java/blob/master/core/src/test/java/io/grpc/internal/DnsNameResolverProviderTest.java#L61
//            for (String s : this.gRpcClientProperties.getNr()) {
//                Preconditions.checkArgument(Strings.isNullOrEmpty(s));
//                if (s.startsWith("dns://")) {
//                    if (dns == null) {
//                        dns = new DnsNameResolverProvider();
//                        syncContext = new SynchronizationContext(
//                                new Thread.UncaughtExceptionHandler() {
//                                    @Override
//                                    public void uncaughtException(Thread t, Throwable e) {
//                                        throw new AssertionError(e);
//                                    }
//                                });
//                    }
//                    NameResolver.Args args = NameResolver.Args.newBuilder()
//                            .setDefaultPort(8080)
//                            .setProxyDetector(GrpcUtil.DEFAULT_PROXY_DETECTOR)
//                            .setSynchronizationContext(syncContext)
//                            .setServiceConfigParser(NameResolver.ServiceConfigParser.class)
//                            .setChannelLogger(ChannelLogger.class)
//                            .build();
//
//                    dns.newNameResolver(URI.create(s));
//                } else {
//                    throw new IllegalArgumentException("NameResolver " + s + " not impl");
//                }
//            }
//
//
//        }

        final var clients = Maps.<Map.Entry<String, GRpcClientProperties.ClientItem>, ManagedChannelBuilder<?>>newHashMap();

        for (var client : this.gRpcClientProperties.getClients().entrySet()) {
            // 目标地址不能为空
            Preconditions.checkArgument(!Strings.isNullOrEmpty(client.getValue().getTarget()));

            if (client.getValue().getTarget().startsWith("dns://")) {
                // 默认 dns
            } else if (client.getValue().getTarget().startsWith("dvip://")) {
                // docker vip
            } else if (client.getValue().getTarget().startsWith("ddnsrr://")) {
                // docker swarm dnsrr
                throw new UnsupportedOperationException();
            } else {
                throw new IllegalArgumentException("UnSupport type " + client.getValue().getTarget());
            }

            // https://github.com/grpc/grpc-java/blob/master/core/src/test/java/io/grpc/internal/DnsNameResolverProviderTest.java#L61
            // https://github.com/grpc/grpc-java/blob/master/api/src/main/java/io/grpc/ManagedChannelBuilder.java#L52
            // 默认 target 支持 dns 发现
            final var builder = NettyChannelBuilder.forTarget(client.getValue().getTarget());

            this.applicationContext.publishEvent(new GrpcClientConfigurationEvent(new GrpcClientConfigurationEntity(client.getKey(), builder)));

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
