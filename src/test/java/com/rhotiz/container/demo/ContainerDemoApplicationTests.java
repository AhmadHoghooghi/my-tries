package com.rhotiz.container.demo;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class ContainerDemoApplicationTests {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Container
	static ConfluentKafkaContainer kafka =  new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.8.0"));

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}



	@Test
	void contextLoads() throws InterruptedException {

		String message = "Hello World";
		CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("topic-1", message);
		future.whenComplete((result, ex) -> {
			if (ex == null) {
				System.out.println("Sent message=[" + message +
						"] with offset=[" + result.getRecordMetadata().offset() + "]");
			} else {
				System.out.println("Unable to send message=[" +
						message + "] due to : " + ex.getMessage());
			}
		});

		Thread.sleep(30_000);

		System.out.println("Kafka image is executed.");
	}

}
