package io.github.jojoti.grpcstatersb.autoconfigure;

import io.github.jojoti.grpcstatersb.GRpcPrimaryService;
import io.github.jojoti.grpcstatersb.GRpcPrivateService;
import io.github.jojoti.grpcstatersb.GRpcServerRunner;
import io.github.jojoti.grpcstatersb.GRpcService;
import io.grpc.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@AutoConfigureOrder
@ConditionalOnBean(annotation = {GRpcService.class, GRpcPrimaryService.class, GRpcPrivateService.class})
@EnableConfigurationProperties(GRpcServerProperties.class)
@Slf4j
public class GRpcAutoConfiguration {

    @Bean
    @GRpcFound
    public GRpcServerRunner grpcServerRunner(GRpcServerProperties gRpcServerProperties, HealthStatusManager healthStatusManager) {
        return new GRpcServerRunner(gRpcServerProperties, healthStatusManager);
    }

    @Bean
    @GRpcFound
    public HealthStatusManager healthStatusManager() {
        return new HealthStatusManager();
    }

}
