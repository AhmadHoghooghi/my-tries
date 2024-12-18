package com.rhotiz.container.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	ConfluentKafkaContainer kafkaContainer() {
		DockerImageName imageName = DockerImageName.parse("confluentinc/cp-kafka:7.8.0");
		return new ConfluentKafkaContainer(imageName);
	}

}
