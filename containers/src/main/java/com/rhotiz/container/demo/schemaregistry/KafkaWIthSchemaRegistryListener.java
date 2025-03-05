package com.rhotiz.container.demo.schemaregistry;

import com.rhotiz.avro.MyMessage;
import com.rhotiz.container.demo.Constants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaWIthSchemaRegistryListener {

    @KafkaListener(topics = Constants.SCHEMA_REGISTRY_TOPIC)
    public void consume(ConsumerRecord<String, MyMessage> record) {
        System.out.println("Received message: " + record.value().getMessage());
    }
}
