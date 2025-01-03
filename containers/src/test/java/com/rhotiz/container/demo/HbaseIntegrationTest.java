package com.rhotiz.container.demo;

import com.rhotiz.container.demo.hbase.HbaseAdminService;
import com.rhotiz.container.demo.hbase.HbaseRepository;
import ir.sahab.testcontainers.DnsAwareGenericContainer;
import jakarta.annotation.PostConstruct;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static com.rhotiz.container.demo.hbase.HbaseConstants.TEST;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {"hbase.interaction.config.enabled=true"})
public class HbaseIntegrationTest {

        static Logger HBASE_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
//    static Logger HBASE_CONTAINER_LOGGER = LoggerFactory.getLogger("HbaseContainer");
    static Logger logger = LoggerFactory.getLogger(HbaseIntegrationTest.class);

    public static final String HBASE_IMAGE = "docker.arvancloud.ir/dajobe/hbase:latest";
    @Container
    @SuppressWarnings("all")
    public static GenericContainer hbase = new DnsAwareGenericContainer(HBASE_IMAGE, "hbase-docker")
            .withExposedPorts(2181, 16000, 16010)
            .withLogConsumer(new Slf4jLogConsumer(HBASE_CONTAINER_LOGGER));

    @DynamicPropertySource
    static void setDynamicPropertiesSources(DynamicPropertyRegistry registry) {
        logger.info("hbase.host: {}", hbase.getHost());
        logger.info("hbase.port: {}", hbase.getMappedPort(2181));
        registry.add("hbase.zookeeper.quorum", () -> hbase.getHost());
        registry.add("hbase.zookeeper.property.clientPort", () -> hbase.getMappedPort(2181));
    }


    @Autowired
    HbaseRepository hbaseRepository;

    @Autowired
    HbaseAdminService hbaseAdminService;

    @Autowired
    Connection connection;

    @Test
    void testRead() throws IOException {
        hbaseAdminService.deleteTableIfExists(TEST);
        hbaseAdminService.createTable(TEST);
        hbaseRepository.put(TEST, "row1", "cf", "a", "value-1");
        byte[] bytes = hbaseRepository.readCell(TEST, "row1", "cf", "a");
        logger.info("read value is: {}", Bytes.toString(bytes));
    }

    @AfterEach
    void closeConnection(){
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
