package com.rhotiz.container.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {"kafka.interaction.config.enabled=true"})
public class HbaseIntegrationTest {

//    static Logger HBASE_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
        static Logger HBASE_CONTAINER_LOGGER = LoggerFactory.getLogger("HbaseContainer");
    static Logger logger = LoggerFactory.getLogger(HbaseIntegrationTest.class);

    @Container
    @SuppressWarnings("rawtypes")
    public static GenericContainer hbase = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/dajobe/hbase:latest"))
            .withExposedPorts(2181);

    @Test
    void test(){

    }
}
