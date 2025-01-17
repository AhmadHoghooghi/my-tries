```shell
docker cp ./files/words.txt spark-worker-1:/tmp/words.txt
docker cp ./files/words.txt spark-worker-2:/tmp/words.txt
```

```shell
docker exec -it spark-master /opt/bitnami/spark/bin/spark-submit \
--master spark://spark-master:7077 \
--deploy-mode cluster \
--class com.rhotiz.spark.WcWithExternalDataSet \
--conf spark.eventLog.enabled=true \
--conf spark.eventLog.dir=file:///tmp/spark-events \
--conf spark.executor.memory=512m \
--conf spark.executor.cores=2 \
--conf spark.driver.memory=512m \
--conf spark.driver.cores=1 \
--conf spark.sql.session.timeZone="Asia/Tehran" \
/tmp/spark-app-jars/spark-app-1.0-SNAPSHOT.jar true
```

To see cached RDD:

Navigate to the Application in spark master UI

Look for your application's appName in the list of running/completed applications.
Click the application ID to view details.

Check the Storage Tab:

Go to the Storage tab in the Spark UI.