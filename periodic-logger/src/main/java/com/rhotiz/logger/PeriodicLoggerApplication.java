package com.rhotiz.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PeriodicLoggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeriodicLoggerApplication.class, args);
	}

}
