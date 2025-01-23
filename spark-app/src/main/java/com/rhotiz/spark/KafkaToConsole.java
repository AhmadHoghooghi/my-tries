package com.rhotiz.spark;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions; // Import Spark SQL functions

public class KafkaToConsole {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("KafkaToConsole")
                .master(System.getenv("SPARK_MASTER_URL"))
                .getOrCreate();

        if (args.length < 1) {
            throw new RuntimeException("Kafka bootstrap server is not defined, pass as args[0]");
        }

        String kafkaBootstrapServers = "localhost:9092";
        String sourceTopic = "source-topic";

        spark
                .read()
                .format("kafka")
                .option("kafka.bootstrap.servers", kafkaBootstrapServers)
                .option("subscribe", sourceTopic)
                .option("startingOffsets", "earliest")
                .option("endingOffsets", "latest")
                .load()
                .limit(3) // Limit to 3 messages
                .selectExpr("CAST(value AS STRING) AS value")
                .withColumn("value", functions.upper(functions.col("value")))
                .collectAsList()
                .forEach(row -> System.out.println(row.getString(0)));

        // Stop the SparkSession
        spark.stop();
    }
}