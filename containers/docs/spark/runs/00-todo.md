# Two workers
- [ ] create and test a docker compose file with two workers each having two thread
# Testing Two Workers
- [ ] test effect of two workers and two thread in each of them with a query for example
# Configure Spark-UI
- [x] How to config master and worker containers so that `spark-ui` shows them well?
I can access the logs of submitted job

`http://7690b33c696e:8081/logPage/?driverId=driver-20250103152008-0000&logType=stdout`

`http://7690b33c696e:8081/logPage/?driverId=driver-20250103152008-0000&logType=stderr`

with changing  `7690b33c696e` to localhost.

this is link of back to master:
`http://31b70d253f48:8080/`

```text
CONTAINER ID   IMAGE                                    COMMAND                  CREATED          STATUS          PORTS                                                                                                      NAMES
31b70d253f48   docker.arvancloud.ir/bitnami/spark:3.5   "/opt/bitnami/script…"   10 minutes ago   Up 10 minutes   0.0.0.0:4040-4050->4040-4050/tcp, :::4040-4050->4040-4050/tcp, 0.0.0.0:8080->8080/tcp, :::8080->8080/tcp   spark-master
7690b33c696e   docker.arvancloud.ir/bitnami/spark:3.5   "/opt/bitnami/script…"   10 minutes ago   Up 10 minutes   0.0.0.0:8081->8081/tcp, :::8081->8081/tcp                                                                  spark-worker
```
this shows that in spark ui is uses container id's for creating links. how can i fix this?

## answer:
solved by setting this property in docker compose for each master and worker: `SPARK_PUBLIC_DNS=localhost`
Description about property:
The public DNS name of the Spark master and workers (default: none).

# Storage
- [ ] Can my current setup with docker compose have storage and check pointing with local file system?

# Submit with REST:
- [ ] How can we submit using REST APIs?

### Answer:
consider these properties from [Spark Standalone Mode](https://spark.apache.org/docs/3.5.4/spark-standalone.html)

- `spark.master.rest.enabled`
- `spark.master.rest.port`

also see this link [Lunching with Rest](https://spark.apache.org/docs/3.5.4/spark-standalone.html#rest-api)

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

# Resource Management.
- [ ] read and understand this part well [resource allocation and configuration overview](https://spark.apache.org/docs/3.5.4/spark-standalone.html#resource-allocation-and-configuration-overview)

# Do dependency management of application jars using maven

# spark ui timezone
this may have effect on it:
user.timezone history server system properties

# mount jar file to default address of jars

# limit memory of drivers to 500M

# Executor is killed.
when i submit a job and get the results, I see the results in standard output of driver and there is no error
in stderr of driver. on the other side, the status of executor is killed and i see this error there:

`25/01/16 07:23:32 ERROR CoarseGrainedExecutorBackend: RECEIVED SIGNAL TERM tdown`

Is there something wrong with the way i end the tasks?

# UI sql tab
Why I don't see sql tab in my UI?

# UI executors tab
Why I don't see executors in my UI?

# UI for metrics
Why  don't see ui for metrics?

# UI for structured streaming
I saw a ui for structured streaming here: https://youtu.be/rNpzrkB5KQQ?si=xB6APitOtFlfWvnk&t=720

# concept of cache? 
this can be used for #optimize_check_list

# a complete docker with local mode 1 for test
with and history server 

