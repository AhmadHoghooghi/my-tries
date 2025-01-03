package com.rhotiz.container.demo;

import com.rhotiz.container.demo.zookeeper.ZookeeperUtil;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
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
@TestPropertySource(properties = {"zookeeper.interaction.config.enabled=true"})
public class ZookeeperIntegrationTest {

    Logger log = LoggerFactory.getLogger(ZookeeperIntegrationTest.class);

    @Container
    @SuppressWarnings("rawtypes")
    public static GenericContainer zooKeeper = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/zookeeper:3.9.3"))
            .withExposedPorts(2181);

    @Autowired
    private CuratorFramework zookeeperCuratorClient;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        String zookeeperConnectionString = String.format("%s:%s", zooKeeper.getHost(), zooKeeper.getFirstMappedPort());
        registry.add("zookeeper.connection-string", () -> zookeeperConnectionString);
    }


    @Test
    void writeAndReadOnZookeeperHappyPath() throws Exception {
        // Create ZNode
        String initialValue = UUID.randomUUID().toString();
        zookeeperCuratorClient.create().forPath("/test-path", ZookeeperUtil.toByteArray(initialValue));
        log.info("created path with value {}", initialValue);

        // Fetch
        String fetchedValue = ZookeeperUtil.toString(zookeeperCuratorClient.getData().forPath("/test-path"));
        log.info("Fetched value from zookeeper: {}", fetchedValue);
        assertThat(fetchedValue, is(equalTo(initialValue)));

        // Update
        String anotherValue = UUID.randomUUID().toString();
        zookeeperCuratorClient.setData().forPath("/test-path", ZookeeperUtil.toByteArray(anotherValue));
        log.info("Updated value too {}", anotherValue);

        // Fetch
        String fetchedValueAgain = ZookeeperUtil.toString(zookeeperCuratorClient.getData().forPath("/test-path"));
        log.info("Fetched value from zookeeper: {}", fetchedValueAgain);
        assertThat(fetchedValueAgain, is(equalTo(anotherValue)));
    }
}
