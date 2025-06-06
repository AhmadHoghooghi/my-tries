
```shell
export SPARK_APP=com.rhotiz.spark.WordCountMasterMaster
```

```shell
docker exec -it spark-master /opt/bitnami/spark/bin/spark-submit \
--master spark://spark-master:7077 \
--deploy-mode cluster \
--class $SPARK_APP \
--conf spark.eventLog.enabled=true \
--conf spark.eventLog.dir=file:///tmp/spark-events \
--conf spark.executor.memory=512m \
--conf spark.executor.cores=2 \
--conf spark.driver.memory=512m \
--conf spark.driver.cores=1 \
/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar
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
