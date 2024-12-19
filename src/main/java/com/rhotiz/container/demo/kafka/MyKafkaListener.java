package com.rhotiz.container.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MyKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(MyKafkaListener.class);
    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = "topic-1")
    public void kafkaListener(String message) {
        messages.add(message);
        log.info("I get this message from kafka: [{}]", message);
    }

    public boolean received(String message) {

        return messages.contains(message);
    }
}