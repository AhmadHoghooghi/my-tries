package com.rhotiz.container.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class ContainerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContainerDemoApplication.class, args);
	}

}
