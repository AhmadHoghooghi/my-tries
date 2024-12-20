package com.rhotiz.container.demo;

import com.rhotiz.container.demo.zookeeper.ZookeeperUtil;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Testcontainers
@PropertySource("")
public class ZookeeperIntegrationTest {

    @Container
    public static GenericContainer zooKeeper = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/zookeeper:3.9.3"))
            .withExposedPorts(2181);

    @Autowired
    private CuratorFramework zookeeperCuratorClient;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        String zookeeperConnectionString = String.format("%s:%s", zooKeeper.getHost(), zooKeeper.getFirstMappedPort());
        System.out.println(zookeeperConnectionString);
        registry.add("zookeeper.connection-string", () -> zookeeperConnectionString);
    }


    @Test
    void writeAndReadOnZookeeperHappyPath() throws Exception {
        String valueOnZooKeeper = UUID.randomUUID().toString();
        zookeeperCuratorClient.create().forPath("/test-path", ZookeeperUtil.toByteArray(valueOnZooKeeper));

        String readValue = ZookeeperUtil.toString(zookeeperCuratorClient.getData().forPath("/test-path"));

        assertThat(readValue, is(equalTo(valueOnZooKeeper)));
    }
}
