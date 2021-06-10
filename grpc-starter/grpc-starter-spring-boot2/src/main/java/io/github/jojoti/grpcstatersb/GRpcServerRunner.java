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

package io.github.jojoti.grpcstatersb;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import io.github.jojoti.grpcstatersb.autoconfigure.GRpcServerProperties;
import io.grpc.*;
import io.grpc.netty.NettyServerBuilder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GRpcServerRunner implements CommandLineRunner, DisposableBean, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(GRpcServerRunner.class);

    private final GRpcServerProperties gRpcServerProperties;

    // 守护线程直到任务结束...
    private final CountDownLatch latch;
    private ImmutableList<MultiServer> servers;
    private ApplicationContext applicationContext;

    public GRpcServerRunner(GRpcServerProperties gRpcServerProperties) {
        this.gRpcServerProperties = gRpcServerProperties;
        // 多个 latch
        this.latch = new CountDownLatch(gRpcServerProperties.getServers().size());
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting gRPC Server ...");

        // 添加到所有 grpc server 的拦截器
        // 拦截器先添加后执行的请 自行使用 spring @Order 注解排序
        final var allGlobalInterceptors = applicationContext.getBeansWithAnnotation(GRpcGlobalInterceptor.class)
                .values()
                .stream()
                .map(c -> (ServerInterceptor) c)
                .collect(Collectors.toUnmodifiableList());

        // 所有 handler
        final var allScopeHandlers = applicationContext.getBeansWithAnnotation(GRpcScopeService.class);

        // 所有分组拦截器
        final var scopeHandlers = Multimaps.<GRpcScope, BindableService>newMultimap(Maps.newHashMap(), Lists::newArrayList);

        for (Object value : allScopeHandlers.values()) {
            // check impl BindableService
            Preconditions.checkArgument(value instanceof BindableService, "Annotation @GRpcScopeService class must be instance of io.grpc.BindableService.");
            // 肯定能找到
            final var foundScope = value.getClass().getAnnotation(GRpcScopeService.class).scope();

            GRpcServerProperties.checkScopeName(foundScope.value());

            scopeHandlers.put(foundScope, (BindableService) value);
        }

        // 所有分组拦截器
        final var scopeInterceptors = Multimaps.<GRpcScope, ServerInterceptor>newMultimap(Maps.newHashMap(), Lists::newArrayList);

        final var allScopeInterceptors = applicationContext.getBeansWithAnnotation(GRpcScopeGlobalInterceptor.class);
        for (Object value : allScopeInterceptors.values()) {
            Preconditions.checkArgument(value instanceof ServerInterceptor, "Annotation @GRpcScopeService class must be instance of io.grpc.ServerInterceptor.");
            // 肯定能找到
            final var foundScope = value.getClass().getAnnotation(GRpcScopeGlobalInterceptor.class).scope();

            GRpcServerProperties.checkScopeName(foundScope.value());

            scopeInterceptors.put(foundScope, (ServerInterceptor) value);
        }

        final var serversBuilder = ImmutableList.<MultiServer>builder();

        for (Map.Entry<GRpcScope, Collection<BindableService>> entry : scopeHandlers.asMap().entrySet()) {
            // 根据 scopeName 读取配置
            final var config = getServerConfigByScopeName(entry.getKey().value());
            final var newServerBuilder = getServerBuilder(config);

            // 遍历添加每个 service
            for (BindableService bindableService : entry.getValue()) {
                final var foundGRpcServiceInterceptors = bindableService.getClass().getAnnotation(GRpcServiceInterceptors.class);

                if (foundGRpcServiceInterceptors == null || foundGRpcServiceInterceptors.applyGlobalInterceptors()) {
                    for (ServerInterceptor allGlobalInterceptor : allGlobalInterceptors) {
                        // 先添加全局拦截器
                        // grpc 拦截器是先添加的后执行
                        newServerBuilder.intercept(allGlobalInterceptor);
                    }
                }

                if (foundGRpcServiceInterceptors == null || foundGRpcServiceInterceptors.applyScopeGlobalInterceptors()) {
                    // 添加局部拦截器
                    final var foundScopeInterceptors = scopeInterceptors.get(entry.getKey());
                    if (foundScopeInterceptors != null && foundScopeInterceptors.size() > 0) {
                        for (ServerInterceptor foundScopeInterceptor : foundScopeInterceptors) {
                            // grpc 拦截器是先添加的后执行
                            newServerBuilder.intercept(foundScopeInterceptor);
                        }
                    }
                }

                // 添加每个 service 特有的拦截器
                if (foundGRpcServiceInterceptors != null && foundGRpcServiceInterceptors.interceptors().length > 0) {
                    final var foundInterceptors = Lists.<ServerInterceptor>newArrayList();
                    for (var interceptor : foundGRpcServiceInterceptors.interceptors()) {
                        final var findDefinedInterceptorBean = applicationContext.getBean(interceptor);
                        // 拦截器定义的 bean 没有找到
                        Preconditions.checkNotNull(findDefinedInterceptorBean, "Class " + interceptor + " ioc bean not found.");
                        foundInterceptors.add(findDefinedInterceptorBean);
                    }
                    ServerInterceptors.intercept(bindableService, foundInterceptors);
                } else {
                    newServerBuilder.addService(bindableService);
                }

                final var server = newServerBuilder.build().start();
                // 如需要注册的 consul 等 在这里发布 event
                log.info("gRPC Server {} started, listening on port {}.", entry.getKey().value(), server.getPort());
                serversBuilder.add(new MultiServer(server, config));
            }
        }

        this.servers = serversBuilder.build();

        if (this.servers.size() != allScopeHandlers.size() || this.servers.size() != this.gRpcServerProperties.getServers().size()) {
            this.destroy();
            // 启动的server与配置的 或者 bean注解的个数不匹配
            throw new IllegalArgumentException("Config error, please check.");
        }

        // 添加守护线程
        Thread awaitThread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("gRPC server awaiter interrupted.", e);
            }
        });
        awaitThread.setName("multi grpc server awaiter");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void destroy() {
        if (this.servers != null) {
            for (var server : this.servers) {
                try {
                    server.server.awaitTermination(server.config.getShutdownGracefullyMills(), TimeUnit.MILLISECONDS);
                    log.info("gRPC server {} stopped.", server.config.getScopeName());
                } catch (InterruptedException e) {
                    log.error("gRPC server {} interrupted during destroy.", server.config.getScopeName(), e);
                } finally {
                    this.latch.countDown();
                }
            }
            this.servers = null;
            log.info("gRPC server stopped.");
        }
    }

    private GRpcServerProperties.ServerItem getServerConfigByScopeName(String scopeName) {
        for (GRpcServerProperties.ServerItem gRpcServerPropertiesServer : this.gRpcServerProperties.getServers()) {
            if (gRpcServerPropertiesServer.getScopeName().equals(scopeName)) {
                return gRpcServerPropertiesServer;
            }
        }
        throw new IllegalArgumentException("ScopeName " + scopeName + " not found, please you check config or annotation.");
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private ServerBuilder<?> getServerBuilder(GRpcServerProperties.ServerItem serverItem) {
        URL url;
        try {
            url = new URL(serverItem.getAddress());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        final var socket = InetSocketAddress.createUnresolved(url.getHost(), url.getPort());
        return NettyServerBuilder.forAddress(socket);
    }

    private static final class MultiServer {
        final Server server;
        final GRpcServerProperties.ServerItem config;

        MultiServer(Server server, GRpcServerProperties.ServerItem config) {
            this.server = server;
            this.config = config;
        }
    }

}
