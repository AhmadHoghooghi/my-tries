package com.rhotiz.container.demo.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "zookeeper.interaction.config.enabled", havingValue = "true")
public class ZooKeeperCuratorConfig {

    @Value("${zookeeper.connection-string}")
    String zookeeperConnectionString;


    @Bean
    CuratorFramework zookeeperCuratorClient(){
        RetryPolicy retryPolicy = new RetryNTimes(3, 100);
        CuratorFramework zookeeperCuratorClient = CuratorFrameworkFactory
                .newClient(zookeeperConnectionString, retryPolicy);
        zookeeperCuratorClient.start();

        return zookeeperCuratorClient;
    }
}
