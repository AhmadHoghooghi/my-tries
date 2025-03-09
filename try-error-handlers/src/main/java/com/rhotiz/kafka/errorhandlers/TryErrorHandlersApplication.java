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
public class TryErrorHandlersApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TryErrorHandlersApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String pathInString = "./executionId.txt";
		Path path = Path.of(pathInString);
		File file = new File(pathInString);
		file.createNewFile();
		List<String> strings = Files.readAllLines(path);
		if(strings.isEmpty()) {
			Files.writeString(path, "A");
		} else {
			char c = (strings.get(strings.size() - 1).trim()).charAt(0);
			c = (char) (c + 1);
			Files.writeString(path, String.valueOf(c));
		}
	}
}
