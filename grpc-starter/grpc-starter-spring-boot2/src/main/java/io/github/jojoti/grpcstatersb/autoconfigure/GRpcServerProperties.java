package io.github.jojoti.grpcstatersb.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties("grpcs")
@Getter
@Setter
public class GRpcServerProperties {

    private List<ServerItem> servers;

    @Getter
    @Setter
    public static final class ServerItem {
        private String scopedName;
        private String address;
    }

}