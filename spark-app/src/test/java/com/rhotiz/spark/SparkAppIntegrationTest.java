package com.rhotiz.spark;

import com.github.dockerjava.api.command.CreateContainerCmd;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.function.Consumer;

@Testcontainers
public class SparkAppIntegrationTest {


//    static Logger MASTER_CONTAINER_LOGGER = LoggerFactory.getLogger("SparkContainer");
    static Logger MASTER_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;

//    static Logger HISTORY_CONTAINER_LOGGER = LoggerFactory.getLogger("HistoryContainer");
    static Logger HISTORY_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;

    static String JAR_FILE_PATH = "/home/ahmad/codes/my-tries/spark-app/target/";

    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static GenericContainer spark = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/bitnami/spark:3.5"))
            .withEnv("SPARK_PUBLIC_DNS", "localhost")
            .withExposedPorts(8080, 4040)
            .withLogConsumer(new Slf4jLogConsumer(MASTER_CONTAINER_LOGGER))
            .withFileSystemBind(JAR_FILE_PATH, "/tmp/spark-app-jars/", BindMode.READ_WRITE)
            .withFileSystemBind("/tmp/spark-cluster/spark-events", "/tmp/spark-events", BindMode.READ_WRITE)
            .waitingFor(Wait.forListeningPorts(8080));

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Container
    public static GenericContainer sparkHistory = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/bitnami/spark:3.5"))
            .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) (CreateContainerCmd cmd) -> cmd.withUser("root"))
            .withEnv("SPARK_PUBLIC_DNS", "localhost")
            .withEnv("SPARK_HISTORY_OPTS", "-Dspark.history.fs.logDirectory=file:///tmp/spark-events")
            .withExposedPorts(18080)
            .withCommand("sh", "-c", "rm -rf /tmp/spark-events/* && chown -R 1001:1001 /tmp/spark-events && ./sbin/start-history-server.sh")
            .withLogConsumer(new Slf4jLogConsumer(HISTORY_CONTAINER_LOGGER))
            .withFileSystemBind("/tmp/spark-cluster/spark-events", "/tmp/spark-events", BindMode.READ_WRITE)
            ;


    @Test
    void runSparkTest() throws InterruptedException, IOException {
        System.out.println("Master: http://localhost:" + spark.getMappedPort(8080));
        System.out.println("APP: http://localhost:" + spark.getMappedPort(4040));
        System.out.println("History: http://localhost:" + sparkHistory.getMappedPort(18080));

        String[] command = {
                "/opt/bitnami/spark/bin/spark-submit",
                "--master", "local[1]",
                "--deploy-mode", "client",
                "--class", "com.rhotiz.spark.WordCountLocalMaster",
                "--conf", "spark.executor.memory=512m",
                "--conf", "spark.executor.cores=1",
                "--conf", "spark.driver.memory=512m",
                "--conf", "spark.driver.cores=1",
                "--conf", "spark.eventLog.enabled=true",
                "--conf", "spark.eventLog.dir=file:///tmp/spark-events",
                "/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar",
                "2"

        };

        org.testcontainers.containers.Container.ExecResult execResult = spark.execInContainer(command);
        System.out.println("execResult.getExitCode() = " + execResult.getExitCode());
        System.out.println("execResult.getStdout() = " + execResult.getStdout());
        System.out.println("execResult.getStderr() = " + execResult.getStderr());


        //submit the application with feign
//        Thread.sleep(3600_000L);
    }

    @AfterAll
    public static void waitForExit() throws InterruptedException {
        boolean keepContainersAliveAfterTest = false;
        if (keepContainersAliveAfterTest) {
            Thread.sleep(3600 * 1000L);
        }
    }

}
