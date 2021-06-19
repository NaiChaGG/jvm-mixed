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

package io.github.jojoti.grpcstartersb;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.net.InetAddresses;
import io.github.jojoti.grpcstartersb.autoconfigure.GRpcServerProperties;
import io.grpc.*;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * rfs:
 * https://github.com/spring-projects/spring-boot/blob/v2.5.1/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/embedded/EmbeddedWebServerFactoryCustomizerAutoConfiguration.java
 * https://github.com/spring-projects/spring-boot/blob/v2.5.1/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/web/reactive/context/WebServerStartStopLifecycle.java
 * https://github.com/spring-projects/spring-boot/blob/v2.5.1/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/web/reactive/context/WebServerManager.java
 * <p>
 * 参考 spring boot 是 如何管理 多种类型的 server
 * 与 spring boot 无关，直接使用 spring context 来管理 grpc
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GRpcServers implements SmartLifecycle, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(GRpcServers.class);

    private final GRpcServerProperties gRpcServerProperties;

    // 守护线程直到任务结束...
    private CountDownLatch latch;
    private List<MultiServer> servers = Lists.newArrayList();
    private ApplicationContext applicationContext;

    private volatile boolean running;

    public GRpcServers(GRpcServerProperties gRpcServerProperties) {
        this.gRpcServerProperties = gRpcServerProperties;
    }

    @Override
    public void start() {
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
            Preconditions.checkArgument(value instanceof BindableService, "Annotation @GRpcScopeService class must be instance of io.grpc.BindableService");

            // 肯定能找到
            final var foundScope = AnnotationUtils.findAnnotation(value.getClass(), GRpcScopeService.class).scope();

            this.gRpcServerProperties.validScopeName(foundScope.value());

            scopeHandlers.put(foundScope, (BindableService) value);
        }

        // 所有分组拦截器
        final var scopeInterceptors = Multimaps.<GRpcScope, ServerInterceptor>newMultimap(Maps.newHashMap(), Lists::newArrayList);

        final var allScopeInterceptors = applicationContext.getBeansWithAnnotation(GRpcScopeGlobalInterceptor.class);
        for (Object value : allScopeInterceptors.values()) {
            Preconditions.checkArgument(value instanceof ServerInterceptor, "Annotation @GRpcScopeService class must be instance of io.grpc.ServerInterceptor");
            // 肯定能找到
            final var foundScope = AnnotationUtils.findAnnotation(value.getClass(), GRpcScopeGlobalInterceptor.class).scope();

            this.gRpcServerProperties.validScopeName(foundScope.value());

            scopeInterceptors.put(foundScope, (ServerInterceptor) value);
        }

        final var serverBuilders = Lists.<ServerBuilders>newArrayList();

        final var services = Multimaps.<GRpcScope, ServiceDescriptor>newListMultimap(Maps.newHashMap(), Lists::newArrayList);

        for (Map.Entry<GRpcScope, Collection<BindableService>> entry : scopeHandlers.asMap().entrySet()) {
            // 根据 scopeName 读取配置
            final var config = getServerConfigByScopeName(entry.getKey().value());
            final var newServerBuilder = getServerBuilder(config);

            final HealthStatusManager health = config.isEnableHealthStatus() ? new HealthStatusManager() : null;

            if (health != null) {
                // 添加健康 检查 service
                newServerBuilder.addService(health.getHealthService());
            }

            // 遍历添加每个 service
            for (BindableService bindableService : entry.getValue()) {
                final var foundGRpcServiceInterceptors = bindableService.getClass().getAnnotation(GRpcServiceInterceptors.class);

                // 拦截器先添加，后执行的

                // 添加每个 service 特有的拦截器
                if (foundGRpcServiceInterceptors != null && foundGRpcServiceInterceptors.interceptors().length > 0) {
                    final var foundInterceptors = Lists.<ServerInterceptor>newArrayList();
                    for (var interceptor : foundGRpcServiceInterceptors.interceptors()) {
                        final var findDefinedInterceptorBean = applicationContext.getBean(interceptor);
                        // 拦截器定义的 bean 没有找到
                        Preconditions.checkNotNull(findDefinedInterceptorBean, "Class " + interceptor + " ioc bean not found");
                        foundInterceptors.add(findDefinedInterceptorBean);
                    }
                    ServerInterceptors.intercept(bindableService, foundInterceptors);
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

                for (ServerInterceptor allGlobalInterceptor : allGlobalInterceptors) {
                    if (allGlobalInterceptor instanceof DynamicScopeFilter) {
                        // 比较两个注解的 scope 是否是同一个里面
                        if (((DynamicScopeFilter) allGlobalInterceptor).getScopes() != null) {
                            for (String scope : ((DynamicScopeFilter) allGlobalInterceptor).getScopes()) {
                                if (entry.getKey().value().equals(scope)) {
                                    newServerBuilder.intercept(allGlobalInterceptor);
                                    break;
                                }
                            }
                        }
                    } else {
                        // 先添加全局拦截器
                        // grpc 拦截器是先添加的后执行
                        newServerBuilder.intercept(allGlobalInterceptor);
                    }
                }

                newServerBuilder.addService(bindableService);

                if (health != null) {
                    health.setStatus(bindableService.bindService().getServiceDescriptor().getName(), HealthCheckResponse.ServingStatus.SERVING);
                }

                serverBuilders.add(new ServerBuilders(newServerBuilder, health, config));
                // 保存所有的 apis
                services.put(entry.getKey(), bindableService.bindService().getServiceDescriptor());
            }
        }

        for (GRpcServerProperties.ServerItem server : this.gRpcServerProperties.getServers()) {
            var exists = false;
            for (ServerBuilders serverBuilder : serverBuilders) {
                if (server.getScopeName().equals(serverBuilder.config.getScopeName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                throw new IllegalArgumentException("Scope " + server.getScopeName() + " exists, but no handler is configured");
            }
        }

        if (serverBuilders.size() != allScopeHandlers.size() || serverBuilders.size() != this.gRpcServerProperties.getServers().size()) {
            // 启动的server与配置的 或者 bean注解的个数不匹配
            throw new IllegalArgumentException("Config error, please check config or annotation");
        }

        // 多个 latch
        this.latch = new CountDownLatch(gRpcServerProperties.getServers().size());

        for (var serverBuilder : serverBuilders) {
            final Server server;

            try {
                server = serverBuilder.serverBuilder.build().start();
            } catch (IOException e) {
                // 打断流程
                log.error("gRPC Server {} start error", serverBuilder.config.getScopeName(), e);
                this.stop();
                // 关闭所有 countDown
                for (long i = 0; i < this.latch.getCount(); i++) {
                    this.latch.countDown();
                }
                throw new RuntimeException(e);
            }

            // 如需要注册的 consul 等 在这里发布 event
            log.info("gRPC Server {} started, listening on port {}", serverBuilder.config.getScopeName(), server.getPort());
            this.servers.add(new MultiServer(server, serverBuilder.healthStatusManager, serverBuilder.config));
        }

        this.running = true;

        // 发布 api 完成注册的事件
        this.applicationContext.publishEvent(new ScopeServicesEvent(new ScopeServicesEventEntities(services)));

        // 添加守护线程
        Thread awaitThread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                log.error("gRPC server awaiter interrupted", e);
            }
        });
        awaitThread.setName("multi grpc server awaiter");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void stop() {
        log.info("grpc server stopping...");
        if (this.servers != null) {
            for (var server : this.servers) {
                if (server.healthStatusManager != null) {
                    for (ServerServiceDefinition service : server.server.getServices()) {
                        server.healthStatusManager.clearStatus(service.getServiceDescriptor().getName());
                    }
                }
                server.server.shutdown();
                try {
                    server.server.awaitTermination(server.config.getShutdownGracefullyMills(), TimeUnit.MILLISECONDS);
                    log.info("gRPC server {} stopped", server.config.getScopeName());
                } catch (InterruptedException e) {
                    log.error("gRPC server {} interrupted during destroy", server.config.getScopeName(), e);
                } finally {
                    try {
                        this.latch.countDown();
                    } catch (Exception e) {
                        log.error("gRpc server {} count down error", server.config.getScopeName(), e);
                    }
                }
            }
            this.servers = null;
            log.info("gRPC server stopped");
        }
        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private GRpcServerProperties.ServerItem getServerConfigByScopeName(String scopeName) {
        for (GRpcServerProperties.ServerItem gRpcServerPropertiesServer : this.gRpcServerProperties.getServers()) {
            if (gRpcServerPropertiesServer.getScopeName().equals(scopeName)) {
                return gRpcServerPropertiesServer;
            }
        }
        throw new IllegalArgumentException("ScopeName " + scopeName + " not found, please you check config or annotation");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private ServerBuilder<?> getServerBuilder(GRpcServerProperties.ServerItem serverItem) {
        // 蛋疼
        URL url;
        try {
            url = new URL("http://" + serverItem.getAddress());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        // fixme 处理 netty netty shared

//        final var socket = InetSocketAddress.createUnresolved(url.getHost(), url.getPort());
        // 使用 guava api 可以监听 127.0.0.1 0.0.0.0 等端口
        return NettyServerBuilder.forAddress(new InetSocketAddress(InetAddresses.forString(url.getHost()), url.getPort()));
    }

    private static final class MultiServer {
        final Server server;
        final HealthStatusManager healthStatusManager;
        final GRpcServerProperties.ServerItem config;

        MultiServer(Server server, HealthStatusManager healthStatusManager, GRpcServerProperties.ServerItem config) {
            this.server = server;
            this.healthStatusManager = healthStatusManager;
            this.config = config;
        }
    }

    private static final class ServerBuilders {
        final ServerBuilder<?> serverBuilder;
        final HealthStatusManager healthStatusManager;
        final GRpcServerProperties.ServerItem config;

        ServerBuilders(ServerBuilder<?> serverBuilder, HealthStatusManager healthStatusManager, GRpcServerProperties.ServerItem config) {
            this.serverBuilder = serverBuilder;
            this.healthStatusManager = healthStatusManager;
            this.config = config;
        }

    }

}
