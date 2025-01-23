```shell
docker exec -it spark-master /opt/bitnami/spark/bin/spark-submit \
--master spark://spark-master:7077 \
--deploy-mode cluster \
--class com.rhotiz.spark.KafkaToConsole \
--conf spark.eventLog.enabled=true \
--conf spark.eventLog.dir=file:///tmp/spark-events \
--conf spark.executor.memory=512m \
--conf spark.executor.cores=2 \
--conf spark.driver.memory=512m \
--conf spark.driver.cores=1 \
--conf spark.sql.session.timeZone="Asia/Tehran" \
/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar "localhost:9092"
```