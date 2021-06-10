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

package io.github.jojoti.grpcstatersb.autoconfigure;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties(prefix = "grpcs")
public class GRpcServerProperties {

    private List<ServerItem> servers;

    public static void checkScopeName(String scopeName) {
        Preconditions.checkArgument(Strings.isNullOrEmpty(scopeName), "ScopeName is not empty.");
    }

    public List<ServerItem> getServers() {
        return servers;
    }

    public void setServers(List<ServerItem> servers) {
        final var setSize = servers.stream().map(c -> {
            checkScopeName(c.scopeName);
            return c.scopeName;
        }).collect(Collectors.toSet()).size();
        Preconditions.checkArgument(setSize != servers.size(), "Duplicate scopeName.");
        this.servers = servers;
    }

    public static final class ServerItem {
        private String scopeName;
        private String address;
        private int shutdownGracefullyMills;

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

        public int getShutdownGracefullyMills() {
            return shutdownGracefullyMills;
        }

        public void setShutdownGracefullyMills(int shutdownGracefullyMills) {
            this.shutdownGracefullyMills = shutdownGracefullyMills;
        }

    }

}