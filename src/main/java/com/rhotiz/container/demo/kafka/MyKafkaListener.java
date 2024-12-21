package com.rhotiz.container.demo.kafka;

import com.rhotiz.container.demo.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(value = "kafka.interaction.config.enabled", havingValue = "true")
public class MyKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(MyKafkaListener.class);
    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = Constants.TOPIC_1)
    public void kafkaListener(String message) {
        messages.add(message);
        log.info("I get this message from kafka: {}", message);
    }

    public boolean received(String message) {
        return messages.contains(message);
    }
}
