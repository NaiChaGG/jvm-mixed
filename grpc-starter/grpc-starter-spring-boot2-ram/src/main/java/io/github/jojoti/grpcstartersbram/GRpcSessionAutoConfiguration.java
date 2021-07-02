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

package io.github.jojoti.grpcstartersbram;

import io.github.jojoti.grpcstartersb.GRpcGlobalInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.List;

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
//@ConditionalOnBean({GRpcServers.class, SessionCreator.class})
//@ConditionalOnExpression("'${grpcs.session}' != null && '${grpcs.session}'.size() > 0")
// 需要 外部 导入了  session 实例
// 且 配置文件里面配置了 session
@Conditional(GRpcSessionAutoConfiguration.EnableSession.class)
@EnableConfigurationProperties(GRpcSessionProperties.class)
public class GRpcSessionAutoConfiguration {

    @Bean
    @GRpcGlobalInterceptor
    @Order(0)
    public SessionInterceptor sessionInterceptor(Session session, GRpcSessionProperties gRpcSessionProperties) {
        return new SessionInterceptor(session, gRpcSessionProperties);
    }

    static final class EnableSession implements Condition {

        private static final Bindable<List<GRpcSessionProperties.SessionItem>> STRING_LIST = Bindable.listOf(GRpcSessionProperties.SessionItem.class);

        // rf: org.springframework.boot.autoconfigure.condition.OnPropertyListCondition
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            var found = Binder.get(conditionContext.getEnvironment()).bind("grpcs.servers", STRING_LIST);
            if (found.isBound()) {
                for (var ramItem : found.get()) {
                    if (ramItem.isEnableSession()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
