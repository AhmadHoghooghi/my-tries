package com.rhotiz.container.demo.schemaregistry;

import com.rhotiz.avro.MyMessage;
import com.rhotiz.container.demo.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@ConditionalOnProperty(value = "schema-registry.interaction.action.enabled", havingValue = "true")
public class KafkaWithSchemaRegistryService {

    @Autowired
    KafkaTemplate<String, MyMessage> kafkaTemplate;

    @Scheduled(fixedRate = 5000L)
    void sendMessage() {
        String messageString = "Hello world from avro message " + LocalTime.now();
        System.out.println("generating message: "+messageString);
        MyMessage message = new MyMessage(messageString);

        kafkaTemplate.send(Constants.SCHEMA_REGISTRY_TOPIC, message);
    }

}
