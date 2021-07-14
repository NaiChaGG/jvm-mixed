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

package io.github.jojoti.grpcstartersb.autoconfigure;

import com.google.common.base.Preconditions;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties(prefix = "grpcs")
public class GRpcServerProperties {

    private Map<String, ServerItem> servers;

    public Map<String, ServerItem> getServers() {
        return servers;
    }

    public void setServers(Map<String, ServerItem> servers) {
        servers.values().forEach(c -> {
            // 至少设置为 1s
            Preconditions.checkArgument(c.shutdownGracefullyMills >= 1000);
        });
        this.servers = servers;
    }

    public static final class HealthStatus {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static final class ServerItem {
        private String address;
        // 默认 5s
        private int shutdownGracefullyMills = 5000;
        // 是否启用健康检查，默认不启用，对终端的api不启动这个，没什么用
        // 但是如果前端有 grpc proxy 则有用 所有使用开关，配置自己决定是否使用
        private HealthStatus healthStatus = new HealthStatus();
        private NettyConfig nettyConfig = null;
        private NettySharedConfig nettySharedConfig = null;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getShutdownGracefullyMills() {
            return shutdownGracefullyMills;
        }

        public void setShutdownGracefullyMills(int shutdownGracefullyMills) {
            this.shutdownGracefullyMills = shutdownGracefullyMills;
        }

        public HealthStatus getHealthStatus() {
            return healthStatus;
        }

        public void setHealthStatus(HealthStatus healthStatus) {
            this.healthStatus = healthStatus;
        }

        public NettyConfig getServerConfig() {
            return nettyConfig;
        }

        public void setServerConfig(NettyConfig nettyConfig) {
            this.nettyConfig = nettyConfig;
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