package com.rhotiz.container.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
public class KafkaSchemaRegistryIntegrationTest {

        static Logger ZOOKEEPER_LOGGER = NOPLogger.NOP_LOGGER;
//    static Logger ZOOKEEPER_LOGGER = LoggerFactory.getLogger("ZookeeperContainer");

        static Logger KAFKA_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
//    static Logger KAFKA_CONTAINER_LOGGER = LoggerFactory.getLogger("KafkaContainer");


    static Logger SCHEMA_REGISTRY_LOGGER = NOPLogger.NOP_LOGGER;
//        static Logger SCHEMA_REGISTRY_LOGGER = LoggerFactory.getLogger("SchemaRegistryContainer");

    public static final int ZOOKEEPER_EXPOSE_PORT = 2181;
    public static final String ZOOKEEPER_NETWORK_ALIAS = "zookeeper";


    static Network network = Network.newNetwork();

    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static GenericContainer zooKeeper = new GenericContainer(
            DockerImageName.parse("docker.arvancloud.ir/confluentinc/cp-zookeeper:7.5.2")
    )
            .withNetwork(network)
            .withNetworkAliases(ZOOKEEPER_NETWORK_ALIAS)
            .withExposedPorts(ZOOKEEPER_EXPOSE_PORT)
            .withEnv("ZOOKEEPER_CLIENT_PORT", String.valueOf(ZOOKEEPER_EXPOSE_PORT))
            .withLogConsumer(new Slf4jLogConsumer(ZOOKEEPER_LOGGER));

    public static final String KAFKA_NETWORK_ALIAS = "kafka";
    public static final int KAFKA_PORT_9092 = 9092;
    public static final int KAFKA_PORT_29092 = 29092;
    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    static GenericContainer kafka = new GenericContainer(
            DockerImageName.parse("docker.arvancloud.ir/confluentinc/cp-kafka:7.5.2")
    )
            .withNetwork(network)
            .withExposedPorts(KAFKA_PORT_9092, KAFKA_PORT_29092)
            .withNetworkAliases(KAFKA_NETWORK_ALIAS)
            .withEnv("KAFKA_BROKER_ID", "1")
            .withEnv("KAFKA_ZOOKEEPER_CONNECT", ZOOKEEPER_NETWORK_ALIAS + ":" + ZOOKEEPER_EXPOSE_PORT)
            .withEnv("KAFKA_LISTENERS", "INTERNAL://0.0.0.0:29092,EXTERNAL://0.0.0.0:9092")
            .withEnv("KAFKA_ADVERTISED_LISTENERS", "INTERNAL://kafka:29092,EXTERNAL://localhost:9092")
            .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT")
            .withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "INTERNAL")
            .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
            .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1")
            .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1")
            .withLogConsumer(new Slf4jLogConsumer(KAFKA_CONTAINER_LOGGER))
            .dependsOn(zooKeeper);

    @Container
    static GenericContainer<?> schemaRegistry = new GenericContainer<>(
            DockerImageName.parse("docker.arvancloud.ir/confluentinc/cp-schema-registry:7.5.2")
    )
            .withNetwork(network)
            .withExposedPorts(8081)
            .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
            .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
            .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "PLAINTEXT://" + KAFKA_NETWORK_ALIAS + ":" + KAFKA_PORT_29092)
            .dependsOn(kafka)
            .withLogConsumer(new Slf4jLogConsumer(SCHEMA_REGISTRY_LOGGER));


    @Test
    public void a() {
        Assertions.assertTrue(zooKeeper.isRunning());
        Assertions.assertTrue(kafka.isRunning());
        Assertions.assertTrue(schemaRegistry.isRunning());
    }
}
