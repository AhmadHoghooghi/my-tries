package com.rhotiz.container.demo;

import org.springframework.boot.SpringApplication;

public class TestContainerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(ContainerDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
