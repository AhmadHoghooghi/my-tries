package com.rhotiz.container.demo;

import com.rhotiz.container.demo.kafka.MyKafkaListener;
import com.rhotiz.container.demo.kafka.SendCallback;
import com.rhotiz.container.demo.zookeeper.ZookeeperUtil;
import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {
        "kafka.interaction.config.enabled=true",
        "zookeeper.interaction.config.enabled=true"})
public class KafkaWithExternalZookeeperIntegrationTest {

    static Logger KAFKA_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
//        static Logger KAFKA_CONTAINER_LOGGER = LoggerFactory.getLogger("KafkaContainer");

    Logger logger = LoggerFactory.getLogger(KafkaWithExternalZookeeperIntegrationTest.class);

    /**
     * These fields are used for introducing zookeeper to kafka instead of using
     * zooKeeper.getHost();
     * zooKeeper.getFirstMappedPort();
     *
     * Mentioned methods are not available while creating kafka container.
     * So note the `withNetworkAliases` in creating zookeeper container
     */

    public static final int ZOOKEEPER_EXPOSE_PORT = 2181;
    public static final String ZOOKEEPER_NETWORK_ALIAS = "zookeeper";

    /**
     * Without network the two containers can not connect
     */
    static Network network = Network.newNetwork();


    /**
     * Mind the withNetworkAliases which is used for introducing zookeeper to kafka.
     * The withNetworkAliases method in Testcontainers allows you to assign a custom alias to a
     * container within the Docker network created for your test environment.
     * This alias can be used as a hostname when other containers within the
     * same network need to communicate with this container.
     * This may solve half of the problem that causes us to have DnsAwareTest Containers.
     * As much as I understand we need DnsAware test containers for two reason:
     * A) Because we need the container to have a specific host
     * B) It is required to have a hostname to communicate with the Container.
     * for part A) see
     * <a href="https://java.testcontainers.org/features/advanced_options/">Customizing the container</a>:
     */
    @Container
    @SuppressWarnings("rawtypes")
    public static GenericContainer zooKeeper = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/zookeeper:3.9.3"))
            .withNetwork(network)
            .withNetworkAliases(ZOOKEEPER_NETWORK_ALIAS)
            .withExposedPorts(ZOOKEEPER_EXPOSE_PORT);

    /**
     * Note that the used docker image subclass of generic container:
     * org.testcontainers.containers.KafkaContainer.
     * It is used to interact with interoperability of zookeeper and kafka.
     * Deprecated:
     * org.testcontainers.containers.KafkaContainer
     * Alternatives:
     * org.testcontainers.kafka.ConfluentKafkaContainer
     * org.testcontainers.kafka.KafkaContainer
     * Note that
     * withExternalFunctionality is only available for org.testcontainers.containers.KafkaContainer (deprecated one)
     */
    @Container
    @ServiceConnection
    @SuppressWarnings("deprecation")
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("docker.arvancloud.ir/confluentinc/cp-kafka:7.8.0")
                    .asCompatibleSubstituteFor("confluentinc/cp-kafka")
    )
            .withNetwork(network)
            .withExternalZookeeper(ZOOKEEPER_NETWORK_ALIAS + ":" + ZOOKEEPER_EXPOSE_PORT)
            .withLogConsumer(new Slf4jLogConsumer(KAFKA_CONTAINER_LOGGER))
            .dependsOn(zooKeeper);


    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        String zookeeperConnectionString = String.format("%s:%s", zooKeeper.getHost(), zooKeeper.getFirstMappedPort());
        registry.add("zookeeper.connection-string", () -> zookeeperConnectionString);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private CuratorFramework zookeeperCuratorClient;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MyKafkaListener myKafkaListener;

    @Test
    void testIntegrationWithZookeeperAndKafkaRunningWithExternalZookeeper() throws Exception {
        // Create ZNode
        String initialValue = UUID.randomUUID().toString();
        zookeeperCuratorClient.create().forPath("/test-path", ZookeeperUtil.toByteArray(initialValue));
        logger.info("created path with value {}", initialValue);

        // Fetch from Zookeeper
        String fetchedValue = ZookeeperUtil.toString(zookeeperCuratorClient.getData().forPath("/test-path"));
        logger.info("Fetched value from zookeeper: {}", fetchedValue);
        assertThat(fetchedValue, is(equalTo(initialValue)));

        // Send to Kafka
        String message = "Unique Message with Id: " + UUID.randomUUID();
        kafkaTemplate.send(Constants.TOPIC_1, fetchedValue)
                .whenComplete(new SendCallback());
        logger.info("Send fired for {}", message);
        logger.info("Waiting to receive message from kafka...");

        // Detect read from kafka
        Awaitility.await().until(() -> myKafkaListener.received(fetchedValue));
    }
}
