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
import com.google.common.collect.*;
import io.github.jojoti.grpcstartersb.autoconfigure.GRpcServerProperties;
import io.github.jojoti.utildaemonthreads.DaemonThreads;
import io.github.jojoti.utilguavaext.GetAddress;
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

import java.util.Collection;
import java.util.Map;
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

    private ImmutableList<MultiServer> servers;
    private ApplicationContext applicationContext;

    private volatile DaemonThreads daemonThreads;

    public GRpcServers(GRpcServerProperties gRpcServerProperties) {
        this.gRpcServerProperties = gRpcServerProperties;
    }

    @Override
    public void start() {
        Preconditions.checkArgument(this.gRpcServerProperties.getServers() != null && this.gRpcServerProperties.getServers().size() > 0, "Servers is not allow empty");

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

            scopeHandlers.put(foundScope, (BindableService) value);
        }

        // 所有分组拦截器
        final var scopeInterceptors = Multimaps.<GRpcScope, ServerInterceptor>newMultimap(Maps.newHashMap(), Lists::newArrayList);

        final var allScopeInterceptors = applicationContext.getBeansWithAnnotation(GRpcScopeGlobalInterceptor.class);
        for (Object value : allScopeInterceptors.values()) {
            Preconditions.checkArgument(value instanceof ServerInterceptor, "Annotation @GRpcScopeService class must be instance of io.grpc.ServerInterceptor");
            // 肯定能找到
            final var foundScope = AnnotationUtils.findAnnotation(value.getClass(), GRpcScopeGlobalInterceptor.class).scope();
            scopeInterceptors.put(foundScope, (ServerInterceptor) value);
        }

        final var serverBuilders = Lists.<ServerBuilders>newArrayList();

        final var services = Multimaps.<GRpcScope, BindableService>newListMultimap(Maps.newHashMap(), Lists::newArrayList);
        // 一个作用域之下 存在 多个 拦截器

        final var scopeInterceptorUtils = new DynamicScopeFilterUtils();

        for (Map.Entry<GRpcScope, Collection<BindableService>> entry : scopeHandlers.asMap().entrySet()) {
            // 根据 scopeName 读取配置
            final var config = getServerConfigByScopeName(entry.getKey().value());
            final var newServerBuilder = getServerBuilder(config);

            // 可以使用 此 api 动态的 扩展 grpc 配置
            // 参考用法: io/github/jojoti/grpcstartersbram/SessionInterceptor.java:72
            this.applicationContext.publishEvent(new GrpcServerBuilderCreateEvent(new GrpcServerBuilderCreate(entry.getKey(), newServerBuilder)));
            // newServerBuilder

            final HealthStatusManager health = config.isEnableHealthStatus() ? new HealthStatusManager() : null;

            if (health != null) {
                log.info("GRPC scopeName {} add health service", entry.getKey().value());
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

                        scopeInterceptorUtils.addCheck(entry.getKey(), findDefinedInterceptorBean);

                        foundInterceptors.add(findDefinedInterceptorBean);
                    }
                    ServerInterceptors.intercept(bindableService, foundInterceptors);
                }

                if (foundGRpcServiceInterceptors == null || foundGRpcServiceInterceptors.applyScopeGlobalInterceptors()) {
                    // 添加局部拦截器
                    final var foundScopeInterceptors = scopeInterceptors.get(entry.getKey());
                    if (foundScopeInterceptors != null && foundScopeInterceptors.size() > 0) {
                        for (ServerInterceptor foundScopeInterceptor : foundScopeInterceptors) {
                            scopeInterceptorUtils.addCheck(entry.getKey(), foundScopeInterceptor);
                            // grpc 拦截器是先添加的后执行
                            newServerBuilder.intercept(foundScopeInterceptor);
                        }
                    }
                }

                if (foundGRpcServiceInterceptors == null || foundGRpcServiceInterceptors.applyGlobalInterceptors()) {
                    for (ServerInterceptor allGlobalInterceptor : allGlobalInterceptors) {
                        if (allGlobalInterceptor instanceof ScopeServerInterceptor) {
                            // 比较两个注解的 scope 是否是同一个里面
                            // 只有超全局拦截器才会用到 动态 tag
                            Preconditions.checkNotNull(((ScopeServerInterceptor) allGlobalInterceptor).getScopes());
                            for (String scope : ((ScopeServerInterceptor) allGlobalInterceptor).getScopes()) {
                                if (entry.getKey().value().equals(scope)) {
                                    scopeInterceptorUtils.addCheck(entry.getKey(), allGlobalInterceptor);
                                    newServerBuilder.intercept(allGlobalInterceptor);
                                    break;
                                }
                            }
                        } else {
                            // 先添加全局拦截器
                            // grpc 拦截器是先添加的后执行
                            scopeInterceptorUtils.addCheck(entry.getKey(), allGlobalInterceptor);
                            newServerBuilder.intercept(allGlobalInterceptor);
                        }
                    }
                }
                newServerBuilder.addService(bindableService);
                if (health != null) {
                    health.setStatus(bindableService.bindService().getServiceDescriptor().getName(), HealthCheckResponse.ServingStatus.SERVING);
                }
                // 保存所有的 apis
                services.put(entry.getKey(), bindableService);
            }

            serverBuilders.add(new ServerBuilders(newServerBuilder, health, entry.getKey().value(), config));
            log.info("GRPC scopeName {} add new builder", entry.getKey().value());
        }

        for (var server : this.gRpcServerProperties.getServers().entrySet()) {
            var exists = false;
            for (ServerBuilders serverBuilder : serverBuilders) {
                if (server.getKey().equals(serverBuilder.scopeName)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                throw new IllegalArgumentException("Scope " + server.getKey() + " exists, but no handler is configured");
            }
        }

        if (serverBuilders.size() != scopeHandlers.asMap().size() || serverBuilders.size() != this.gRpcServerProperties.getServers().size()) {
            // 启动的server与配置的 或者 bean注解的个数不匹配
            throw new IllegalArgumentException("Config error, please check config or annotation");
        }

        // 发送 message 到 对应的 拦截器 通知到 bean 注册成功的消息
        for (var entry : scopeInterceptorUtils.getRef().asMap().entrySet()) {
            var found = services.get(entry.getKey());
            Preconditions.checkNotNull(found);
            for (ScopeServerInterceptor scopeServerInterceptor : entry.getValue()) {
                scopeServerInterceptor.aware(entry.getKey(), ImmutableList.copyOf(found));
            }
        }

        final var daemon = DaemonThreads.newDaemonThreads(this.gRpcServerProperties.getServers().size(),
                "Multi grpc server awaiter", (handler, e) -> {
                    log.error("E: {}", handler, e);
                });

        final var serversBuilder = ImmutableList.<MultiServer>builder();

        for (var serverBuilder : serverBuilders) {
            try {
                daemon.startThreads(serverBuilder.scopeName, () -> {
                    final var server = serverBuilder.serverBuilder.build().start();
                    // 如需要注册的 consul 等 在这里发布 event
                    log.info("GRPC Server {} started, listening on port {}", serverBuilder.scopeName, server.getPort());
                    serversBuilder.add(new MultiServer(server, serverBuilder.healthStatusManager, serverBuilder.scopeName, serverBuilder.config));
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        this.daemonThreads = daemon;
        this.servers = serversBuilder.build();

        // fixme 暂无需要使用的场景
//        this.applicationContext.publishEvent(new ScopeServicesEvent(new ScopeServicesEventEntities(services)));
    }

    @Override
    public void stop() {
        log.info("grpc server stopping...");
        if (this.servers != null) {
            for (var server : this.servers) {
                this.daemonThreads.downThreads(server.scopeName, () -> {
                    if (server.healthStatusManager != null) {
                        for (ServerServiceDefinition service : server.server.getServices()) {
                            server.healthStatusManager.clearStatus(service.getServiceDescriptor().getName());
                        }
                    }
                    server.server.shutdown();
                    server.server.awaitTermination(server.config.getShutdownGracefullyMills(), TimeUnit.MILLISECONDS);
                    log.info("gRPC server {} stopped", server.scopeName);
                });
            }
            this.servers = null;
            log.info("gRPC server all stopped");
        }
    }

    @Override
    public boolean isRunning() {
        return this.daemonThreads != null && this.daemonThreads.isHealth();
    }

    private GRpcServerProperties.ServerItem getServerConfigByScopeName(String scopeName) {
        for (var gRpcServerPropertiesServer : this.gRpcServerProperties.getServers().entrySet()) {
            if (gRpcServerPropertiesServer.getKey().equals(scopeName)) {
                return gRpcServerPropertiesServer.getValue();
            }
        }
        throw new IllegalArgumentException("ScopeName " + scopeName + " not found, please you check config or annotation");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private ServerBuilder<?> getServerBuilder(GRpcServerProperties.ServerItem serverItem) {
        // fixme 处理 netty netty shared
        return NettyServerBuilder.forAddress(GetAddress.getSocketAddress(serverItem.getAddress()));
    }

    private static final class MultiServer {
        final Server server;
        final HealthStatusManager healthStatusManager;
        final String scopeName;
        final GRpcServerProperties.ServerItem config;

        MultiServer(Server server, HealthStatusManager healthStatusManager, String scopeName, GRpcServerProperties.ServerItem config) {
            this.server = server;
            this.healthStatusManager = healthStatusManager;
            this.scopeName = scopeName;
            this.config = config;
        }
    }

    private static final class ServerBuilders {
        final ServerBuilder<?> serverBuilder;
        final HealthStatusManager healthStatusManager;
        final String scopeName;
        final GRpcServerProperties.ServerItem config;

        ServerBuilders(ServerBuilder<?> serverBuilder, HealthStatusManager healthStatusManager, String scopeName, GRpcServerProperties.ServerItem config) {
            this.serverBuilder = serverBuilder;
            this.healthStatusManager = healthStatusManager;
            this.scopeName = scopeName;
            this.config = config;
        }
    }

    /**
     * 动态 scope 会 clone 对象
     */
    private static final class DynamicScopeFilterUtils {

        private final Multimap<ScopeServerInterceptor, GRpcScope> ref = Multimaps.newSetMultimap(Maps.newHashMap(), Sets::newHashSet);

        public void addCheck(GRpcScope scope, Object object) {
            if (object instanceof ScopeServerInterceptor) {
                var serverInterceptor = (ScopeServerInterceptor) object;
                if (serverInterceptor.getScopes().contains(scope.value())) {
                    // 这个作用域下 同一个 拦截器实例
                    ref.put(serverInterceptor, scope);
                }
            }
        }

        public Multimap<GRpcScope, ScopeServerInterceptor> getRef() {
            Multimap<GRpcScope, ScopeServerInterceptor> newRef = Multimaps.newSetMultimap(Maps.newHashMap(), Sets::newHashSet);
            for (var entry : this.ref.asMap().entrySet()) {
                for (GRpcScope gRpcScope : entry.getValue()) {
                    var found = newRef.get(gRpcScope);
                    if (found == null || found.size() <= 0) {
                        newRef.put(gRpcScope, entry.getKey());
                    } else {
                        newRef.put(gRpcScope, entry.getKey().cloneThis());
                    }
                }
            }
            return newRef;
        }

    }

}
