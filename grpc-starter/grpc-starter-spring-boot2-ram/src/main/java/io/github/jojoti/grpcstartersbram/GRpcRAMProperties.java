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

package io.github.jojoti.grpcstartersbram;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties(prefix = "grpcs")
public class GRpcRAMProperties {

    // 那些 scope 需要启用 ram 拦截
    private List<ServerRam> servers;

    public List<ServerRam> getServers() {
        return servers;
    }

    public void setServers(List<ServerRam> servers) {
        this.servers = servers;
    }

    public static final class ServerRam {
        private boolean enableRAM = false;

        public boolean isEnableRAM() {
            return enableRAM;
        }

        public void setEnableRAM(boolean enableRAM) {
            this.enableRAM = enableRAM;
        }
    }

}