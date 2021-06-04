package io.github.jojoti.grpcstatersb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.SocketUtils;

import java.util.Optional;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties("grpc")
@Getter
@Setter
public class GRpcServerProperties {

    public static final int DEFAULT_GRPC_PORT = 6565;

    private Integer port = null;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private volatile Integer runningPort = null;

    /**
     * In process server name.
     * If  the value is not empty, the embedded in-process server will be created and started.
     *
     */
    private String inProcessServerName;

    /**
     * Enables server reflection using <a href="https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md">ProtoReflectionService</a>.
     * Available only from gRPC 1.3 or higher.
     */
    private boolean enableReflection = false;

    /**
     * Number of seconds to wait for preexisting calls to finish before shutting down.
     * A negative value is equivalent to an infinite grace period
     */
    private int shutdownGrace = 0;

    public Integer getRunningPort() {
        if (null == runningPort) {
            synchronized (this) {
                if (null == runningPort) {
                    runningPort = Optional.ofNullable(port)
                            .map(p -> 0 == p ? SocketUtils.findAvailableTcpPort() : p)
                            .orElse(DEFAULT_GRPC_PORT);
                }
            }
        }
        return runningPort;

    }

}
