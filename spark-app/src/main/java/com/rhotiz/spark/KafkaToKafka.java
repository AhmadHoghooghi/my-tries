package com.rhotiz.spark;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import java.util.concurrent.TimeoutException;

public class KafkaToKafka {
    public static void main(String[] args) throws StreamingQueryException, TimeoutException {
        // Initialize SparkSession
        SparkSession spark = SparkSession
                .builder()
                .appName("KafkaToKafka")
                .master(System.getenv("SPARK_MASTER_URL"))
                .getOrCreate();

        // Define Kafka source parameters
        String kafkaBootstrapServers = "localhost:9092"; // Kafka broker addresses
        String sourceTopic = "source-topic"; // Kafka source topic
        String sinkTopic = "sink-topic"; // Kafka sink topic

        // Read from Kafka source topic
        StreamingQuery query = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", kafkaBootstrapServers)
                .option("subscribe", sourceTopic)
                .load()
                .selectExpr("CAST(value AS STRING)") // Cast value to string
                .withColumn("value", functions.upper(functions.col("value"))) // Transform value to uppercase
                .selectExpr("CAST(value AS STRING) AS value") // Ensure the column is named "value"
                .writeStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", kafkaBootstrapServers)
                .option("topic", sinkTopic)
                .option("checkpointLocation", "/tmp/checkpoint") // Checkpoint location for fault tolerance
                .start();
        // Wait for the streaming query to terminate
        query.awaitTermination();
    }
}