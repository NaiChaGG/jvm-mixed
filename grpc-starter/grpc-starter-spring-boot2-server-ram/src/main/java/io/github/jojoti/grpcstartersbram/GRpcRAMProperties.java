/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
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

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties(prefix = "grpcs")
public class GRpcRAMProperties {

    // 那些 scope 需要启用 ram 拦截
    private Map<String, RAMItem> servers;

    public Map<String, RAMItem> getServers() {
        return servers;
    }

    public void setServers(Map<String, RAMItem> servers) {
        this.servers = servers;
    }

    List<String> enableScopeNames() {
        var found = Lists.<String>newArrayList();
        for (Map.Entry<String, RAMItem> entry : servers.entrySet()) {
            if (entry.getValue().getRam().enabled) {
                found.add(entry.getKey());
            }
        }
        if (found.size() <= 0) {
            throw new IllegalArgumentException("Bug fix ram scope conditional error");
        }
        return found;
    }

    static final class RAMItem {
        // 访问控制默认打开
        private RAMConfig ram = new RAMConfig();

        public RAMConfig getRam() {
            return ram;
        }

//        public RAMConfig getRam() {
//            return ram == null ? new RAMConfig() : ram;
//        }

        public void setRam(RAMConfig ram) {
            this.ram = ram;
        }
    }

    static final class RAMConfig {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

    }

}