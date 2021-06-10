/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti.
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