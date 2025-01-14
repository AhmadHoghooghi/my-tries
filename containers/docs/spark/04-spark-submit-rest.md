```shell
curl -XPOST http://localhost:6066/v1/submissions/create \
--header "Content-Type:application/json;charset=UTF-8" \
--data '{
  "appResource": "",
  "sparkProperties": {
    "spark.master": "spark://master:7077",
    "spark.app.name": "SimpleUpperCaseWorldCount",
    "spark.driver.memory": "1g",
    "spark.driver.cores": "1",
    "spark.jars": "/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar",
    "spark.driver.extraJavaOptions": "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED",
    "spark.eventLog.enabled": "true",
    "spark.eventLog.dir": "file:///tmp/spark-events"
  },
  "clientSparkVersion": "",
  "mainClass": "com.rhotiz.spark.WordCountLocalMaster",
  "environmentVariables": { },
  "action": "CreateSubmissionRequest",
  "appArgs": []
}'
```

GET status by submission id:
```shell
curl -XGET http://localhost:6066/v1/submissions/status/driver-20250109093001-0002
```
Kill
```shell
curl -XPOST http://localhost:6066/v1/submissions/kill/driver-20250109093001-0002 \
--header "Content-Type:application/json;charset=UTF-8" \
--data '{}'
```


why to pass the below option?
```json
{
  "sparkProperties": {
    "spark.driver.extraJavaOptions": "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED"
  }
}
```

If we do not pass this option we get below error:
```text
Caused by: java.lang.IllegalAccessError: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x250674c)
 cannot access class sun.nio.ch.DirectBuffer (in module java.base) because module
 java.base does not export sun.nio.ch to unnamed module @0x250674c
```

But why only we get this error when submitting with REST API and do not this error when suing `spark-submit`?

When you use spark-submit, it executes directly within the spark-master container. This means: The `spark-submit` 
command runs as a local process in the container, inheriting the container's runtime environment, including:
* Configurations.
* Predefined environment variables.
* Proper access to the Java runtime.

In contrast, the Spark REST API creates a new driver process based on the environment variables and configurations
specified in the REST submission. 
This process may encounter issues if the configuration isn't complete or compatible with the Java runtime.