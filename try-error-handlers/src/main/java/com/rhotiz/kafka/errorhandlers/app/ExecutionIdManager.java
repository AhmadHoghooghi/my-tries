package com.rhotiz.kafka.errorhandlers.app;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class ExecutionIdManager {

    @PostConstruct
    public void manageExecutionId() throws IOException {
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
