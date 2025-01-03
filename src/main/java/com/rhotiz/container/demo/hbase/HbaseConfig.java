package com.rhotiz.container.demo.hbase;

import jakarta.annotation.PostConstruct;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "hbase.interaction.config.enabled", havingValue = "true")
public class HbaseConfig {

    @Value("${hbase.zookeeper.quorum}")
    private String hbaseZookeeperQuorum;

    @Value("${hbase.zookeeper.property.client-port}")
    private String hbaseZookeeperClientPort;

    @Bean
    public Connection connection() throws IOException {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", hbaseZookeeperQuorum);
        conf.set("hbase.zookeeper.property.clientPort", hbaseZookeeperClientPort); //2181 is the default port
        return ConnectionFactory.createConnection(conf);
    }

}
