/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
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

package io.github.jojoti.grpcstartersb.autoconfigure;

import io.github.jojoti.grpcstartersb.GRpcGlobalInterceptor;
import io.github.jojoti.grpcstartersb.GRpcScopeService;
import io.github.jojoti.grpcstartersb.GRpcServers;
import io.github.jojoti.grpcstartersb.GlobalExceptionInterceptor;
import io.grpc.ServerInterceptor;
import io.grpc.util.TransmitStatusRuntimeExceptionInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * rfs:
 * https://github.com/spring-projects/spring-boot/blob/v2.5.1/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/embedded/EmbeddedWebServerFactoryCustomizerAutoConfiguration.java
 * https://github.com/spring-projects/spring-boot/blob/v2.5.1/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/web/reactive/context/WebServerStartStopLifecycle.java
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration(proxyBeanMethods = false)
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@ConditionalOnBean(annotation = {GRpcScopeService.class})
@EnableConfigurationProperties(GRpcServerProperties.class)
public class GRpcAutoConfiguration {

    @Bean
    public GRpcServers grpcServerRunner(GRpcServerProperties gRpcServerProperties) {
        return new GRpcServers(gRpcServerProperties);
    }

    @Bean
    @GRpcGlobalInterceptor
    @Order(22)
    public ServerInterceptor transmitStatusRuntimeExceptionInterceptor() {
        return TransmitStatusRuntimeExceptionInterceptor.instance();
    }

    @Bean
    @GRpcGlobalInterceptor
    @Order(11)
    public ServerInterceptor globalExceptionInterceptor() {
        return GlobalExceptionInterceptor.newGlobalExceptionInterceptor();
    }

}
