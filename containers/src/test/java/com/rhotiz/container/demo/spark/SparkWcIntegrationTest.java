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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = {"logging.level.feign=DEBUG",
        "logging.level.com.rhotiz.container.demo.spark.dto.SparkSubmitClient=DEBUG"})
public class SparkWcIntegrationTest {

    static {
        packageJarFile();
    }

    //    static Logger MASTER_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
    //    static Logger WORKER_CONTAINER_LOGGER = NOPLogger.NOP_LOGGER;
    static Logger MASTER_CONTAINER_LOGGER = LoggerFactory.getLogger("MasterContainer");
    static Logger WORKER_CONTAINER_LOGGER = LoggerFactory.getLogger("WorkerContainer");

    static Network network = Network.newNetwork();


    static String JAR_FILE_PATH = "/home/ahmad/codes/my-tries/spark-app/target/";
    public static final String SPARK_MASTER_NETWORK_ALIAS = "spark-master";
    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static GenericContainer sparkMaster = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/bitnami/spark:3.5"))
            .withEnv("SPARK_MODE", "master")
            .withEnv("SPARK_MASTER_OPTS", "-Dspark.master.rest.enabled=true")
            .withEnv("SPARK_PUBLIC_DNS", "localhost")
            .withExposedPorts(8080, 6066)
            .withLogConsumer(new Slf4jLogConsumer(MASTER_CONTAINER_LOGGER))
            .withNetwork(network)
            .withNetworkAliases(SPARK_MASTER_NETWORK_ALIAS)
            .withFileSystemBind(JAR_FILE_PATH, "/tmp/spark-app-jars/", BindMode.READ_WRITE);


    @Container
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static GenericContainer sparkWorker = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/bitnami/spark:3.5"))
            .withEnv("SPARK_MODE", "worker")
            .withEnv("SPARK_MASTER_URL", "spark://" + SPARK_MASTER_NETWORK_ALIAS + ":" + 7077)
            .withEnv("SPARK_WORKER_CORES", "2")
            .withEnv("SPARK_WORKER_MEMORY", "1100m")
            .withEnv("SPARK_PUBLIC_DNS", "localhost")
            .withExposedPorts(8081)
            .withLogConsumer(new Slf4jLogConsumer(WORKER_CONTAINER_LOGGER))
            .withNetwork(network)
            .withFileSystemBind(JAR_FILE_PATH, "/tmp/spark-app-jars/", BindMode.READ_WRITE);

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        System.out.println(
                "Master:    http://localhost:" + sparkMaster.getMappedPort(8080) + "\n" +
                        "Worker:    http://localhost:" + sparkWorker.getMappedPort(8081) + "\n"
                /* + "APP:       http://localhost:" + sparkWorker.getMappedPort(4040)*/
        );

        registry.add("spark-submit.rest.port", () -> sparkMaster.getMappedPort(6066));
    }

    @Autowired
    private SparkRestClient sparkRestClient;


    @Test
    void runSparkTest() throws InterruptedException, MavenInvocationException {
        CreateSubmissionResponseDto submitResponse = sparkRestClient.submitSparkJob(createSparkSubmitJson());
        System.out.println(submitResponse);
        assertThat(submitResponse.isSuccess(), is(true));


        Awaitility.await()
                .pollInterval(Duration.ofSeconds(5))
                .forever()
                .until(() -> {
                    SubmissionStatusResponseDto jobStatus = sparkRestClient.getJobStatus(submitResponse.getSubmissionId());
                    System.out.println(jobStatus);
                    return jobStatus.getDriverState() == DriverState.FINISHED;
                });


        //submit the application with feign
        Thread.sleep(60_000L);
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


    private CreateSubmissionRequestDto createSparkSubmitJson() {
        return CreateSubmissionRequestDto.builder()
                .appResource("/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar")
                .sparkProperties(Map.of(
                        "spark.master", "local[*]",
                        "spark.app.name", "WordCountLocalMaster",
                        "spark.driver.memory", "512m",
                        "spark.driver.cores", "1",
                        "spark.jars", "/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar",
                        "spark.driver.extraJavaOptions", "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
                        "spark.eventLog.enabled", "false",
                        "spark.deploy.mode", "client"
                ))
                .clientSparkVersion("3.5.4")
                .mainClass("com.rhotiz.spark.WordCountLocalMaster")
                .environmentVariables(Map.of())
                .appArgs(List.of())
                .build();


    }
}
