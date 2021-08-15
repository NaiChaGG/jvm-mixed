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

package io.github.jojoti.grpcstartersbcli.autoconfigure;

import io.github.jojoti.grpcstartersbcli.GrpcClients;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

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
@EnableConfigurationProperties(GRpcClientProperties.class)
@Conditional(GRpcClientAutoConfiguration.EnableGrpcClient.class)
public class GRpcClientAutoConfiguration {

    @Bean
    public GrpcClients grpcClients(GRpcClientProperties gRpcClientProperties) {
        return new GrpcClients(gRpcClientProperties);
    }

    static final class EnableGrpcClient implements Condition {

        private static final Bindable<Map<String, GRpcClientProperties.ClientItem>> STRING_LIST = Bindable.mapOf(String.class, GRpcClientProperties.ClientItem.class);

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            var found = Binder.get(context.getEnvironment()).bind("grpcs.clients", STRING_LIST);
            // clients 需要大于 0 才会开始启动 grpc clients
            return found.isBound() && found.get().size() > 0;
        }

    }

}
