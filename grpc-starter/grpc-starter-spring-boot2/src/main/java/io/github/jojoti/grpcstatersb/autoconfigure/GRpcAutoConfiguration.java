package io.github.jojoti.grpcstatersb.autoconfigure;

import io.github.jojoti.grpcstatersb.*;
import io.grpc.ServerBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@AutoConfigureOrder
@ConditionalOnBean(annotation = {GRpcService.class, GRpcPrimaryService.class, GRpcPrivateService.class})
@EnableConfigurationProperties(GRpcServerProperties.class)
@Slf4j
public class GRpcAutoConfiguration {

    @Autowired
    private GRpcServerProperties grpcServerProperties;

    @Bean
    @ConditionalOnProperty(value = "grpc.enabled", havingValue = "true", matchIfMissing = true)
    public GRpcServerRunner grpcServerRunner(@Qualifier("grpcInternalConfigurator") Consumer<ServerBuilder<?>> configurator) {
        return new GRpcServerRunner(configurator, ServerBuilder.forPort(grpcServerProperties.getRunningPort()));
    }

    @Bean
    @ConditionalOnExpression("#{environment.getProperty('grpc.inProcessServerName','')!=''}")
    public GRpcServerRunner grpcInprocessServerRunner(@Qualifier("grpcInternalConfigurator") Consumer<ServerBuilder<?>> configurator) {
        return new GRpcServerRunner(configurator, InProcessServerBuilder.forName(grpcServerProperties.getInProcessServerName()));
    }

    @Bean
    public HealthStatusManager healthStatusManager() {
        return new HealthStatusManager();
    }

    @Bean
    @ConditionalOnMissingBean(GRpcServerBuilderConfigurer.class)
    public GRpcServerBuilderConfigurer serverBuilderConfigurer() {
        return new GRpcServerBuilderConfigurer();
    }

    @Bean(name = "grpcInternalConfigurator")
    public Consumer<ServerBuilder<?>> configurator(GRpcServerBuilderConfigurer configurer) {
        return serverBuilder -> {
//            if (grpcServerProperties.isEnabled()) {
//
//            }
            configurer.configure(serverBuilder);
        };
    }

}
