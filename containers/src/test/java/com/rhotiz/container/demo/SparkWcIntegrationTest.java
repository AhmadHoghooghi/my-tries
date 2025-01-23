package com.rhotiz.container.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.Transferable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class SparkWcIntegrationTest {

//    static Logger KAFKA_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
        static Logger KAFKA_CONTAINER_LOGGER = LoggerFactory.getLogger(SparkWcIntegrationTest.class);
    static Logger logger = LoggerFactory.getLogger(SparkWcIntegrationTest.class);

    static Network network = Network.newNetwork();


    static String JAR_FILE_PATH = "/home/ahmad/codes/my-tries/spark-app/target/";
    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static GenericContainer spark = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/bitnami/spark:3.5"))
            .withEnv("SPARK_MASTER_OPTS", "-Dspark.master.rest.enabled=true")
            .withEnv("SPARK_PUBLIC_DNS", "localhost")
            .withExposedPorts(8080, 6066)
            .withLogConsumer(new Slf4jLogConsumer(logger))
            .withFileSystemBind(JAR_FILE_PATH, "/tmp/spark-app-jars/", BindMode.READ_WRITE);


    @Test
    void containerSetup() throws InterruptedException {
        System.out.println("Spark is up");

        //submit the application with feign
        Thread.sleep(30000L);
    }
}
