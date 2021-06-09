package io.github.jojoti.grpcstatersb.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties(prefix = "grpcs")
public class GRpcServerProperties {

    private List<ServerItem> servers;

    public static final class ServerItem {
        private String scopedName;
        private String address;

        public String getScopedName() {
            return scopedName;
        }

        public void setScopedName(String scopedName) {
            this.scopedName = scopedName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public List<ServerItem> getServers() {
        return servers;
    }
}