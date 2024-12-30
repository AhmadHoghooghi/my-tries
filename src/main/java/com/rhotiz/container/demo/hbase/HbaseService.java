package com.rhotiz.container.demo.hbase;

import jakarta.annotation.PostConstruct;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.rhotiz.container.demo.hbase.HbaseConstants.TEST;

@Service
@ConditionalOnProperty(name = "hbase.interaction.action.enabled", havingValue = "true")
public class HbaseService {
    Logger logger = LoggerFactory.getLogger(HbaseService.class);


    @Autowired
    HbaseRepository hbaseRepository;

    @Autowired
    HbaseAdminService hbaseAdminService;

    @PostConstruct
    void testRead() throws IOException {
        hbaseAdminService.deleteTableIfExists(TEST);
        hbaseAdminService.createTable(TEST);
        hbaseRepository.put(TEST, "row1", "cf", "a", "value-1");
        byte[] bytes = hbaseRepository.readCell(TEST, "row1", "cf", "a");
        logger.info("read value is: {}", Bytes.toString(bytes));
    }

}
