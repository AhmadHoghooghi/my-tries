You're correct that there is a duplication in setting the master URL in both your `SparkConf` object and the `spark-submit` command. This is because both methods are used to specify the master URL, but they serve slightly different purposes and have different precedence rules.

### Key Points:
1. **Setting the Master in `SparkConf`**:
    - When you set the master URL in the `SparkConf` object (e.g., `.setMaster("local[*]")`), it is part of the application's configuration.
    - This is useful when you want to hardcode the master URL within your application code.

2. **Setting the Master in `spark-submit`**:
    - When you specify the master URL in the `spark-submit` command (e.g., `--master local[*]`), it overrides the master URL specified in the `SparkConf` object.
    - This is useful when you want to dynamically specify the master URL at runtime without modifying the application code.

### Precedence:
- The master URL specified in the `spark-submit` command takes precedence over the one set in the `SparkConf` object.
- If you don't specify the master URL in either place, Spark will default to `local[*]` (local mode with all available cores).

### Is One of Them Optional?
- Yes, one of them is optional. You can choose to set the master URL in either the `SparkConf` object or the `spark-submit` command, but you don't need to set it in both places.
- If you set it in both places, the `spark-submit` command will override the `SparkConf` setting.

### Documentation Reference:
- The official Spark documentation provides details on how to configure and submit Spark applications. You can refer to the following links:
    - [Spark Configuration](https://spark.apache.org/docs/latest/configuration.html)
    - [Submitting Applications](https://spark.apache.org/docs/latest/submitting-applications.html)

### Example:
If you want to make the master URL configurable at runtime, you can omit it from the `SparkConf` object and only specify it in the `spark-submit` command:

```java
SparkConf conf = new SparkConf()
                .setAppName("WordCountLocalMaster")
                .set("spark.cores.max", "1");
```

Then, submit your application with:

```bash
spark-submit --master local[*] --class your.main.class your-app.jar
```

This approach makes your application more flexible, as you can easily switch between different master URLs (e.g., `local`, `yarn`, `k8s`, etc.) without modifying the code.

### Conclusion:
- You don't need to specify the master URL in both places. Choose one based on your use case.
- The `spark-submit` command's `--master` option will override the `SparkConf` setting if both are provided.