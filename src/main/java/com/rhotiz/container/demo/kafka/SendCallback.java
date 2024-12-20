package com.rhotiz.container.demo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;

import java.util.function.BiConsumer;

public class SendCallback implements BiConsumer<SendResult<String, String>, Throwable> {

    private static final Logger log = LoggerFactory.getLogger(SendCallback.class);

    @Override
    public void accept(SendResult<String, String> result, Throwable throwable) {
        if (throwable == null) {
            log.info("Sent message={} with offset={}",
                    result.getProducerRecord().value(),
                    result.getRecordMetadata().offset()
            );
        } else {
            SendCallback.log.error("PROBLEM: Unable to send message={} due to : {}",
                    result.getProducerRecord().value() ,
                            throwable.getMessage());
        }
    }
}