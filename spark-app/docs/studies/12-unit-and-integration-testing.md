# Q1: How to run tests after package with maven command line
add `maven-failsafe-plugin` to project (pom.xml)

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.5.2</version>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
And run tests with `mven clean verify`. verify is executed after package.

Note: Only tests with specific Naming pattern are included by default for Unit and Integration testing:
Unit Testing: [Inclusions and Exclusions of Tests for Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/examples/inclusion-exclusion.html)
Integration Testing [Inclusions and Exclusions of Tests for Failsafe](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/inclusion-exclusion.html)


# Q: How to run tests after package in IntellijIdea UI
- Run The test by greed run button next to test method or class name to easily create Run/Debug Configuration
- Edit the Run/Debug Configuration
- Modify Options / Run before lunch task
- run maven goal
- package

# Run only one test from command line
For Unit Tests, Read more [Here](https://maven.apache.org/surefire/maven-surefire-plugin/examples/single-test.html)
```shell
mvn -Dtest=TestClassName test
mvn -Dtest=TestClassName#testMethodName test
```

For integration Tests, Read more [Here](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/single-test.html)
```shell
mvn -Dit.test=TestClassName verify
mvn -Dit.test=TestClassName#testMethodName verify
```

# How to skip tests
Unit Tests, Read more [Here](https://maven.apache.org/surefire/maven-surefire-plugin/examples/skipping-tests.html)
```shell
mvn install -DskipTests=false
```
Integration Tests, Read more [Here]()

```shell
mvn install -DskipITs
```

If you absolutely must, you can also use the maven.test.skip property to skip compiling the tests. maven.test.skip is honored by Surefire, Failsafe and the Compiler Plugin.
```shell
mvn install -Dmaven.test.skip=true
```

# Run tests with only one Container

# Run a separate history server

for history server It is required to give the ownership of log directory to user with id 1001.
the ownership of user is given to root while creating and mounting directory with docker.
to solve this problem read [Work With Non-Root Containers at broadcom.com](https://techdocs.broadcom.com/us/en/vmware-tanzu/application-catalog/tanzu-application-catalog/services/tac-doc/apps-tutorials-work-with-non-root-containers-index.html) and do this:
```java
@Container
public static GenericContainer sparkHistory = new GenericContainer(DockerImageName.parse("docker.arvancloud.ir/bitnami/spark:3.5"))
        .withCreateContainerCmdModifier((Consumer<CreateContainerCmd>) (CreateContainerCmd cmd) -> cmd.withUser("root")) // <-- run as root user
        .withEnv("SPARK_PUBLIC_DNS", "localhost")
        .withEnv("SPARK_HISTORY_OPTS", "-Dspark.history.fs.logDirectory=file:///tmp/spark-events")
        .withExposedPorts(18080)
        .withCommand("sh", "-c", "rm -rf /tmp/spark-events/* && chown -R 1001:1001 /tmp/spark-events && ./sbin/start-history-server.sh") // <-- change ownership here
        .withLogConsumer(new Slf4jLogConsumer(HISTORY_CONTAINER_LOGGER))
        .withFileSystemBind("/tmp/spark-cluster/spark-events", "/tmp/spark-events", BindMode.READ_WRITE)
        ;
```
## do we need master to run
no, but it's difficult to edit entrypoint and command of bitnami image

## Understand bitnami entrypoint and command 
I studied it.


# Run hbase test without dns-aware test container