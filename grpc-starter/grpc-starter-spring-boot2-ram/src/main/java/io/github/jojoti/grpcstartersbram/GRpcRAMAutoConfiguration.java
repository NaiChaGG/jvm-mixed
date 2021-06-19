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
import io.github.jojoti.grpcstartersb.GRpcServers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rfs:
 * https://stackoverflow.com/questions/48840190/spring-expression-language-java-8-foreach-or-stream-on-list
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration(proxyBeanMethods = false)
// grpc server 正常启动
@ConditionalOnBean(GRpcServers.class)
@ConditionalOnExpression(value = "false")
@EnableConfigurationProperties(GRpcRAMProperties.class)
public class GRpcRAMAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RAMAccess.class)
    public RAMAccess ramAccess() {
        return new RAMAccessDeny();
    }

    @Bean
    @GRpcGlobalInterceptor
    public RAMInterceptor ramInterceptor(RAMAccess ramAccess, GRpcRAMProperties gRpcRAMProperties) {
        return new RAMInterceptor(ramAccess, gRpcRAMProperties);
    }

}
