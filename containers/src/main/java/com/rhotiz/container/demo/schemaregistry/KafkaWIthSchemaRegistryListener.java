package com.rhotiz.container.demo.schemaregistry;

import com.rhotiz.avro.MyMessage;
import com.rhotiz.container.demo.Constants;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@ConditionalOnProperty(value = "schema-registry.interaction.config.enabled", havingValue = "true")
public class KafkaWIthSchemaRegistryListener {

    @Getter
    private final List<MyMessage> messages = new ArrayList<>();

    @KafkaListener(topics = Constants.SCHEMA_REGISTRY_TOPIC)
    public void consume(ConsumerRecord<String, MyMessage> record) {
        messages.add(record.value());
        System.out.println("Received message: " + record.value().getMessage());
    }
}
