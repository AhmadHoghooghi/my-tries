read these content:

* https://spark.apache.org/docs/3.5.4/cluster-overview.html#monitoring
* https://spark.apache.org/docs/3.5.4/monitoring.html
* And this description from chat gpt:

We have three monitoring pages:
* Spark Master: with port 8080
* Spark Worker: with port 8081
* Spark Application UI with port: 4040 and so on (e.g. 4041)

The Spark application UI typically runs on port 4040 for the first application. 

Once the application runs, you can access the Spark UI by visiting:

http://localhost:4040 (for the first Spark application).

If you launch multiple applications concurrently, subsequent applications will use ports 4041, 4042, and so on.

To confirm the port used by your Spark application:

Check the logs of the application:

docker logs <application-container-name>

Starting Spark application UI at http://<host>:4040

Alternatively, if you're using a Spark driver, it prints the UI URL to the console output.

Note that in cluster mode driver runs on one of worker machines. so in docker compose the worker should expose the port.


