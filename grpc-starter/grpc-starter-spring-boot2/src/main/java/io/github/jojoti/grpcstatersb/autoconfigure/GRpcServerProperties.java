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
        private String scopeName;
        private String address;

        public String getScopeName() {
            return scopeName;
        }

        public void setScopeName(String scopeName) {
            this.scopeName = scopeName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public void setServers(List<ServerItem> servers) {

        this.servers = servers;
    }

    public List<ServerItem> getServers() {
        return servers;
    }
}