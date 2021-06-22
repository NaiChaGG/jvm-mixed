package io.github.jojoti.grpcstartersb;

import org.springframework.context.ApplicationEvent;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class GrpcServerBuilderCreateEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public GrpcServerBuilderCreateEvent(GrpcServerBuilderCreate source) {
        super(source);
    }

}
