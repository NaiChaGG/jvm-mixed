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

package io.github.jojoti.grpcstartersbcli.autoconfigure;

import io.github.jojoti.grpcstartersbcli.GrpcClients;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 *
 * rfs:
 *  https://github.com/spring-projects/spring-boot/blob/v2.5.1/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/embedded/EmbeddedWebServerFactoryCustomizerAutoConfiguration.java
 *  https://github.com/spring-projects/spring-boot/blob/v2.5.1/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/web/reactive/context/WebServerStartStopLifecycle.java
 *
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration(proxyBeanMethods = false)
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@GRpcClientAutoConfiguration.ConditionalOnPropertyNotEmpty(value = "grpc")
@EnableConfigurationProperties(GRpcClientProperties.class)
public class GRpcClientAutoConfiguration {

    @Bean
    public GrpcClients grpcServerRunner(GRpcClientProperties gRpcClientProperties) {
        return new GrpcClients(gRpcClientProperties);
    }

    /**
     *  https://stackoverflow.com/questions/46118782/spel-conditionalonproperty-string-property-empty-or-nulll
     */
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Conditional(ConditionalOnPropertyNotEmpty.OnPropertyNotEmptyCondition.class)
    public @interface ConditionalOnPropertyNotEmpty {
        String value();

        class OnPropertyNotEmptyCondition implements Condition {

            @Override
            public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
                Map<String, Object> attrs = metadata.getAnnotationAttributes(ConditionalOnPropertyNotEmpty.class.getName());
                String propertyName = (String) attrs.get("value");
                String val = context.getEnvironment().getProperty(propertyName);
                return val != null && !val.trim().isEmpty();
            }
        }
    }

}
