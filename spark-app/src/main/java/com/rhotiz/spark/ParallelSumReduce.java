package com.rhotiz.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelSumReduce {

    public static void main(String[] args) {
        String appName = ParallelSumReduce.class.getSimpleName() + LocalTime.now(ZoneId.of("Asia/Tehran"));
        SparkConf conf = new SparkConf().setAppName(appName)
                .setMaster(System.getenv("SPARK_MASTER_URL"));
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {

            List<Integer> numbers = IntStream.range(0, 100).boxed().collect(Collectors.toList());
            JavaRDD<Integer> rdd = sc.parallelize(numbers,5);
            Integer sum = rdd.reduce(Integer::sum);
            System.out.println("sum = " + sum);
            sc.stop();
        }
    }
}
