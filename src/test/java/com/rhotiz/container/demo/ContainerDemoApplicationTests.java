package com.rhotiz.container.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ContainerDemoApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Kafka image is executed.");
	}

}
