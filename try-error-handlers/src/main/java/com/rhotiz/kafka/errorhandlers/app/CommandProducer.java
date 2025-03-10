package com.rhotiz.kafka.errorhandlers.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CommandProducer {
    @Autowired
    private KafkaTemplate<String, MyCommand> kafkaTemplate;
    private int commandId = 1;


    @Scheduled(fixedRate = 5*60*1000L)
    public void sendCommands() {
        String executionId = ExecutionUtil.getExecutionId();
        MyCommand myCommand1 = new MyCommand(executionId, commandId++, 1);
        System.out.println("Sending "+ myCommand1);
        kafkaTemplate.send(Constants.TOPIC, myCommand1);
        MyCommand myCommand2 = new MyCommand(executionId, commandId++, 0);
        System.out.println("Sending "+ myCommand2);
        kafkaTemplate.send(Constants.TOPIC, myCommand2);
    }
}
