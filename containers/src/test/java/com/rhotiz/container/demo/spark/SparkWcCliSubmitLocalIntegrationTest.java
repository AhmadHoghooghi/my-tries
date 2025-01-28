package com.rhotiz.container.demo.spark;

import com.rhotiz.container.demo.spark.dto.*;
import org.apache.maven.shared.invoker.*;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * It seems this works even though I can not confirm it because the executed application is not shown in
 * spark ui of master. I may be able to confirm execution with exposing 4040 and sleeping in app
 */

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {"logging.level.feign=DEBUG",
        "logging.level.com.rhotiz.container.demo.spark.dto.SparkSubmitClient=DEBUG"})
public class SparkWcCliSubmitLocalIntegrationTest {

    static {
        packageJarFile();
    }

    static Logger MASTER_CONTAINER_LOGGER = LoggerFactory.getLogger("SparkContainer");


    static String JAR_FILE_PATH = "/home/ahmad/codes/my-tries/spark-app/target/";
    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static GenericContainer spark = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/bitnami/spark:3.5"))
            .withEnv("SPARK_PUBLIC_DNS", "localhost")
            .withExposedPorts(8080)
            .withLogConsumer(new Slf4jLogConsumer(MASTER_CONTAINER_LOGGER))
            .withFileSystemBind(JAR_FILE_PATH, "/tmp/spark-app-jars/", BindMode.READ_WRITE);


    @Test
    void runSparkTest() throws InterruptedException, IOException {
        System.out.println("Master: http://localhost:" + spark.getMappedPort(8080));

        String[] command = {
                "/opt/bitnami/spark/bin/spark-submit",
                "--master", "local[*]",
                "--deploy-mode", "client",
                "--class", "com.rhotiz.spark.WordCountLocalMaster",
                "--conf", "spark.executor.memory=512m",
                "--conf", "spark.executor.cores=1",
                "--conf","spark.driver.memory=512m",
                "--conf","spark.driver.cores=1",
                "/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar"};

        ExecResult execResult = spark.execInContainer(command);
        System.out.println("execResult.getExitCode() = " + execResult.getExitCode());
        System.out.println("execResult.getStdout() = " + execResult.getStdout());
        System.out.println("execResult.getStderr() = " + execResult.getStderr());


        //submit the application with feign
        Thread.sleep(3600_000L);
    }

    private static void packageJarFile() {
        InvocationRequest request = new DefaultInvocationRequest();
        String projectRelativePath = "./../spark-app";
        request.setPomFile(new File(projectRelativePath + "/pom.xml"));
        request.setJavaHome(new File(System.getProperty("java.home")));
        request.setBatchMode(true);
        request.setShellEnvironmentInherited(true);
        request.addArgs(List.of("clean", "package"));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenExecutable(new File(projectRelativePath + "/mvnw"));
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            throw new RuntimeException(e);
        }
    }
}
