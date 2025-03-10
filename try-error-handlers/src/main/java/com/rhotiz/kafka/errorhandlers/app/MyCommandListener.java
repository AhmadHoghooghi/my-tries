package com.rhotiz.kafka.errorhandlers.app;

import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.BatchListenerFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class MyCommandListener {

    @KafkaListener(topics = Constants.TOPIC, groupId = "my-group-id")
    public void listen(List<ConsumerRecord<String, MyCommand>> consumerRecords, Acknowledgment ack) {
        System.out.println();
        System.out.println("PROCESSING BATCH::::[STARTED]");
        for (int i = 0; i < consumerRecords.size(); i++) {
            MyCommand myCommand = consumerRecords.get(i).value();
            System.out.println("Received message: " + myCommand);

            if (myCommand.getFailureCount() > 0) {
                String message = "Handling my command failed for : " + myCommand;
//                throw new RuntimeException(message);
                throw new BatchListenerFailedException(message, consumerRecords.get(i));
            }
        }
        System.out.println("PROCESSING BATCH::::[COMPLETED]");
        ack.acknowledge();
    }
}