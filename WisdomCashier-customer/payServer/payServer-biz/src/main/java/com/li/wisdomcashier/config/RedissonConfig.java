package com.li.wisdomcashier.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RedissonConfig {

    @Resource
    private RedissonConfigProperties redissonConfigProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        String redisPort = redissonConfigProperties.getPort();
        Integer redisTimeout = redissonConfigProperties.getTimeout();
        String redisPassword = redissonConfigProperties.getPassword();
        RedissonConfigProperties.Cluster cluster = redissonConfigProperties.getCluster();
        RedissonConfigProperties.Sentinel sentinel = redissonConfigProperties.getSentinel();

        // 集群
        if (cluster != null && !CollectionUtils.isEmpty(cluster.getNodes())) {
            List<String> nodes = cluster.getNodes();
            List<String> collect = nodes.stream().map(x -> x.startsWith("redis://") ? x : "redis://" + x).toList();
            ClusterServersConfig serverConfig = config.useClusterServers()
                    .setScanInterval(2000)
                    .addNodeAddress(collect.toArray(new String[collect.size()]));
            if (StringUtils.isNotBlank(redisPassword)) {
                serverConfig.setPassword(redisPassword);
            }
        }
        //哨兵
        else if (sentinel != null && !CollectionUtils.isEmpty(sentinel.getNodes())) {
            List<String> nodes = sentinel.getNodes();
            List<String> collect = nodes.stream().map(x -> x.startsWith("redis://") ? x : "redis://" + x).toList();
            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .setMasterName(sentinel.getMaster())
                    .addSentinelAddress(collect.toArray(new String[collect.size()]))
                    .setTimeout(redisTimeout);
            if (StringUtils.isNotBlank(redisPassword)) {
                serverConfig.setPassword(redisPassword);
            }
        } else {
            // 单机配置
            String redisHost = redissonConfigProperties.getHost();
            redisHost = redisHost.startsWith("redis://") ? redisHost : "redis://" + redisHost;
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(redisHost + ":" + redisPort)
                    .setTimeout(redisTimeout);

            if (StringUtils.isNotBlank(redisPassword)) {
                serverConfig.setPassword(redisPassword);
            }
        }
        return Redisson.create(config);
    }
}
