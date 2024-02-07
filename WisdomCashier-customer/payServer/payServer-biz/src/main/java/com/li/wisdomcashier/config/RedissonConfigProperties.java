package com.li.wisdomcashier.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
public class RedissonConfigProperties {
    private String host;
    private String port;
    private String password;
    private Cluster cluster;
    private Integer timeout;
    private Sentinel sentinel;

    @Data
    public static class Sentinel {
        private String master;

        private List<String> nodes;

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }

    @Data
    public static class Cluster {
        private List<String> nodes;

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }
}
