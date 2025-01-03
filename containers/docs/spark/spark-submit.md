Submit from the Spark master container

Steps:
Copy the JAR file to the Spark master container.
Use the spark-submit command inside the Spark master container to submit the application.

```shell

```

```shell
export SPARK_APP_PATH=../../../spark-app
mvn clean package --file $SPARK_APP_PATH/pom.xml
docker cp $SPARK_APP_PATH/target/spark-app-1.0-SNAPSHOT.jar spark-master:/tmp/spark-app-1.0-SNAPSHOT.jar
docker cp $SPARK_APP_PATH/target/spark-app-1.0-SNAPSHOT.jar spark-worker:/tmp/spark-app-1.0-SNAPSHOT.jar
```
```shell
export SPARK_APP=com.rhotiz.spark.SimpleWordCount
```

```shell
docker exec -it spark-master /opt/bitnami/spark/bin/spark-submit \
--master spark://spark-master:7077 \
--deploy-mode cluster \
--class $SPARK_APP \
/tmp/spark-app-1.0-SNAPSHOT.jar
```
see the result of submit here [http://localhost:8080/](http://localhost:8080/)

Why This is Preferred:

The Spark master is designed to manage resource allocation for your Spark jobs. When you submit a job to the Spark master, it orchestrates the work distribution to the Spark workers.
It keeps your workflow centralized, as the Spark master is the control point for the cluster.


How to use fat or uber jar:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.4.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <!-- Main class for your application -->
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>com.example.Main</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

```shell
mvn clean package
```

# Question 1:
How can i submit using REST APIs?

# Question 2:
this is a sample submit for [here](https://spark.apache.org/docs/3.5.4/submitting-applications.html#launching-applications-with-spark-submit)
```shell
# Run application locally on 8 cores
./bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master local[8] \
/path/to/examples.jar \
100
```
and in docker compose we have passed such an option `SPARK_WORKER_CORES=1`. How can these two configs correlate?
