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

package io.github.jojoti.grpcstatersb.autoconfigure;

import io.github.jojoti.grpcstatersb.GRpcScopeService;
import io.github.jojoti.grpcstatersb.GRpcServerRunner;
import io.grpc.services.HealthStatusManager;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@AutoConfigureOrder
@ConditionalOnBean(annotation = {GRpcScopeService.class})
@EnableConfigurationProperties(GRpcServerProperties.class)
public class GRpcAutoConfiguration {

    @Bean
    public GRpcServerRunner grpcServerRunner(GRpcServerProperties gRpcServerProperties, HealthStatusManager healthStatusManager) {
        return new GRpcServerRunner(gRpcServerProperties, healthStatusManager);
    }

    @Bean
    public HealthStatusManager healthStatusManager() {
        return new HealthStatusManager();
    }

}
