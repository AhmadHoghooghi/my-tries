package com.rhotiz.kafka.errorhandlers.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExecutionUtil {

    static String getExecutionId() {
        try {
            String pathInString = "./executionId.txt";
            Path path = Path.of(pathInString);
            List<String> strings = null;
            strings = Files.readAllLines(path);
            char c = (strings.get(strings.size() - 1).trim()).charAt(0);
            c = (char) (c + 1);
            return String.valueOf(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
