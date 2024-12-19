package com.rhotiz.container.demo.kafka;

import com.rhotiz.container.demo.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "my-kafka.producer.enabled", havingValue = "true")
public class MyKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(MyKafkaProducer.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Scheduled(fixedRate = 10_000L)
    void sendMessageToKafka() {
        String message = "Scheduled Message: " + UUID.randomUUID();
        kafkaTemplate.send(Constants.TOPIC_1, message).whenComplete(new SendCallback());
        log.info("Send fired by scheduler on message: {}", message);
    }
}