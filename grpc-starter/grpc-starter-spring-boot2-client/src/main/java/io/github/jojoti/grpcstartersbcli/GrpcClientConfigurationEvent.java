package io.github.jojoti.grpcstartersbcli;

import org.springframework.context.ApplicationEvent;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GrpcClientConfigurationEvent extends ApplicationEvent {

    private final GrpcClientConfigurationEntity entity;

     GrpcClientConfigurationEvent(GrpcClientConfigurationEntity source) {
        super(source);
        this.entity = source;
    }

    public GrpcClientConfigurationEntity getEntity() {
        return entity;
    }

}
