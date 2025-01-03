package com.rhotiz.container.demo;

import com.rhotiz.container.demo.kafka.MyKafkaListener;
import com.rhotiz.container.demo.kafka.SendCallback;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {"kafka.interaction.config.enabled=true"})
class KafkaKRaftIntegrationTest {

    static Logger KAFKA_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
    //    static Logger KAFKA_CONTAINER_LOGGER = LoggerFactory.getLogger("KafkaContainer");
    static Logger logger = LoggerFactory.getLogger(KafkaKRaftIntegrationTest.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MyKafkaListener myKafkaListener;

    @Container
    @ServiceConnection
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(
            DockerImageName.parse("docker.arvancloud.ir/confluentinc/cp-kafka:7.8.0")
                    .asCompatibleSubstituteFor("confluentinc/cp-kafka")
    ).withLogConsumer(new Slf4jLogConsumer(KAFKA_CONTAINER_LOGGER));


    @Test
    void sendMessageAndReceiveHappyPath() {
        String message = "Unique Message with Id: " + UUID.randomUUID();
        kafkaTemplate.send(Constants.TOPIC_1, message)
                .whenComplete(new SendCallback());
        logger.info("Send fired for {}", message);
        logger.info("Waiting to receive message from kafka...");
        Awaitility.await().until(() -> myKafkaListener.received(message));
    }
}
