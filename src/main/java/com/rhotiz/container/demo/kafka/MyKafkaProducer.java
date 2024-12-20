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

import java.util.Random;

@Slf4j
@Service
@ConditionalOnProperty(value = "interaction-with-kafka.enabled", havingValue = "true")
public class MyKafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(MyKafkaProducer.class);

    Random random = new Random();
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Scheduled(fixedRate = 10_000L)
    void sendMessageToKafka() {
        String message = "Scheduled Message: [" + random.nextInt(1000) + "]";
        kafkaTemplate.send(Constants.TOPIC_1, message).whenComplete(new SendCallback());
        log.info("Send fired by scheduler on message: {}", message);
    }
}