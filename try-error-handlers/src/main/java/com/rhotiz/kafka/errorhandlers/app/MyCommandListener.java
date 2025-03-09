package com.rhotiz.kafka.errorhandlers.app;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MyCommandListener {

    @KafkaListener(topics = Constants.TOPIC, groupId = "my-group-id")
    public void listen(MyCommand myCommand) {
        System.out.println("Received message: " + myCommand);

        if (myCommand.getFailureCount() > 0) {
            throw new RuntimeException("Handling my command failed for : "+ myCommand);
        }
    }
}