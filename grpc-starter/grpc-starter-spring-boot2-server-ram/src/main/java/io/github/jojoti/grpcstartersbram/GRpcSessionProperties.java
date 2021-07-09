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

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@ConfigurationProperties(prefix = "grpcs")
public class GRpcSessionProperties {

    // 那些 scope 需要启用 ram 拦截
    private Map<String, SessionItem> servers;

    List<String> enableScopeNames() {
        var found = Lists.<String>newArrayList();
        for (Map.Entry<String, SessionItem> entry : servers.entrySet()) {
            if (entry.getValue().isEnableSession()) {
                found.add(entry.getKey());
            }
        }
        if (found.size() <= 0) {
            throw new IllegalArgumentException("Bug fix session scope conditional error");
        }
        return found;
    }

    public Map<String, SessionItem> getServers() {
        return servers;
    }

    public void setServers(Map<String, SessionItem> servers) {
        this.servers = servers;
    }

    static final class SessionItem {
        private boolean enableSession = false;

        public boolean isEnableSession() {
            return enableSession;
        }

        public void setEnableSession(boolean enableSession) {
            this.enableSession = enableSession;
        }
    }

}