package io.github.jojoti.grpcstartersb;

import java.util.List;
import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class DiscoveryConfig {

    private Dns dns = null;
    private Etcd etcd = null;
    private Consul consul = null;

    public Consul getConsul() {
        return consul;
    }

    public void setConsul(Consul consul) {
        this.consul = consul;
    }

    public Etcd getEtcd() {
        return etcd;
    }

    public void setEtcd(Etcd etcd) {
        this.etcd = etcd;
    }

    public Dns getDns() {
        return dns;
    }

    public void setDns(Dns dns) {
        this.dns = dns;
    }

    public static final class Dns {
        // 后续还可以配置 dns 查询 ttl 等
        /**
         * vip 网络配置 service-name -> vip address
         */
        private Map<String, String> targets = null;

        public Map<String, String> getTargets() {
            return targets;
        }

        public void setTargets(Map<String, String> targets) {
            this.targets = targets;
        }

    }

    public static final class Etcd {
        private List<String> endpoints = null;

        public List<String> getEndpoints() {
            return endpoints;
        }

        public void setEndpoints(List<String> endpoints) {
            this.endpoints = endpoints;
        }
    }

    public static final class Consul {
        private String agent = null;

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }
    }

}


