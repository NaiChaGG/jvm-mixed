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

package io.github.jojoti.grpcstartersbcli.autoconfigure;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.github.jojoti.grpcstartersb.DiscoveryConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties(prefix = "grpc")
public class GRpcClientProperties {

    private List<ClientItem> clients;

    private static void checkServiceName(String serviceName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(serviceName), "ServiceName is not empty");
    }

    public void validServiceName(String serviceName) {
        for (ClientItem server : this.getClients()) {
            if (server.getServiceName().equals(serviceName)) {
                return;
            }
        }
        throw new IllegalArgumentException("ServiceName " + serviceName + " config key not found");
    }

    public List<ClientItem> getClients() {
        return clients;
    }

    public void setClients(List<ClientItem> clients) {
        final var setSize = clients.stream().map(c -> {
            checkServiceName(c.serviceName);
            return c.serviceName;
        }).collect(Collectors.toSet()).size();

        Preconditions.checkArgument(setSize == clients.size(), "Duplicate serviceName");
        this.clients = clients;
    }

    public static final class ClientItem {
        private String serviceName;
        // 默认 5s
        private int shutdownGracefullyMills = 5000;
        private NettyConfig nettyConfig = null;
        private NettySharedConfig nettySharedConfig = null;
        private DiscoveryConfig discovery;

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public NettyConfig getNettyConfig() {
            return nettyConfig;
        }

        public void setNettyConfig(NettyConfig nettyConfig) {
            this.nettyConfig = nettyConfig;
        }

        public NettySharedConfig getNettySharedConfig() {
            return nettySharedConfig;
        }

        public void setNettySharedConfig(NettySharedConfig nettySharedConfig) {
            this.nettySharedConfig = nettySharedConfig;
        }

        public DiscoveryConfig getDiscovery() {
            return discovery;
        }

        public void setDiscovery(DiscoveryConfig discovery) {
            this.discovery = discovery;
        }

        public NettyConfig checkOrGetNettyConfig() {
            if (this.nettyConfig != null) {
                return this.nettyConfig;
            }
            if (this.nettySharedConfig == null) {
                this.nettyConfig = new NettyConfig();
                return this.nettyConfig;
            }
            // netty shared 配置存在
            return null;
        }

        public int getShutdownGracefullyMills() {
            return shutdownGracefullyMills;
        }

        public void setShutdownGracefullyMills(int shutdownGracefullyMills) {
            this.shutdownGracefullyMills = shutdownGracefullyMills;
        }
    }

    public static final class NettyConfig {
        private int maxInboundSize = 1024;

        public int getMaxInboundSize() {
            return maxInboundSize;
        }

        public void setMaxInboundSize(int maxInboundSize) {
            this.maxInboundSize = maxInboundSize;
        }
    }

    public static final class NettySharedConfig {
        private int maxInboundSize = 1024;

        public int getMaxInboundSize() {
            return maxInboundSize;
        }

        public void setMaxInboundSize(int maxInboundSize) {
            this.maxInboundSize = maxInboundSize;
        }
    }

}