read these content:

* https://spark.apache.org/docs/3.5.4/cluster-overview.html#monitoring
* https://spark.apache.org/docs/3.5.4/monitoring.html
* And this description from chat gpt:

The Spark UI typically runs on port 4040 for the first application. 
```shell
version: '3.8'

services:
  spark-master:
    image: docker.io/bitnami/spark:latest
    container_name: spark-master
    environment:
      - SPARK_MODE=master
      - SPARK_RPC_AUTHENTICATION_ENABLED=no
      - SPARK_RPC_ENCRYPTION_ENABLED=no
      - SPARK_LOCAL_STORAGE_ENCRYPTION_ENABLED=no
      - SPARK_SSL_ENABLED=no
    ports:
      - "8080:8080" # Spark Master Web UI
      - "4040-4050:4040-4050" # Spark Application UIs

  spark-worker-1:
    image: docker.io/bitnami/spark:latest
    container_name: spark-worker-1
    environment:
      - SPARK_MODE=worker
      - SPARK_MASTER_URL=spark://spark-master:7077
      - SPARK_RPC_AUTHENTICATION_ENABLED=no
      - SPARK_RPC_ENCRYPTION_ENABLED=no
      - SPARK_LOCAL_STORAGE_ENCRYPTION_ENABLED=no
      - SPARK_SSL_ENABLED=no
    depends_on:
      - spark-master
    ports:
      - "8081:8081" # Spark Worker Web UI
```

Once the application runs, you can access the Spark UI by visiting:

http://localhost:4040 (for the first Spark application).

If you launch multiple applications concurrently, subsequent applications will use ports 4041, 4042, and so on.

To confirm the port used by your Spark application:

Check the logs of the application:

docker logs <application-container-name>

Starting Spark application UI at http://<host>:4040

Alternatively, if you're using a Spark driver, it prints the UI URL to the console output.

