package com.rhotiz.kafka.errorhandlers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@EnableScheduling
@SpringBootApplication
public class TryErrorHandlersApplication {

	public static void main(String[] args) {
		SpringApplication.run(TryErrorHandlersApplication.class, args);
	}

}
