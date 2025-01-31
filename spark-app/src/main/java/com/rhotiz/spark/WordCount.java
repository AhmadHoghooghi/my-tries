package com.rhotiz.spark;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class WordCount {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setAppName("WordCountLocalMaster")
                .set("spark.cores.max", "1");

        int numOfSlices;
        if (args.length < 1) {
            numOfSlices = 1;
        } else {
            numOfSlices = Integer.parseInt(args[0]);
        }

        int sleepSeconds;
        if (args.length < 2) {
            sleepSeconds = 0;
        } else {
            sleepSeconds = Integer.parseInt(args[1]);
        }
//
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {

            // Step 2: Hardcoded string to process
            String input = "hello world hello spark spark is awesome";

            // Step 3: Convert the string into an RDD
            JavaRDD<String> lines = sc.parallelize(List.of(input), numOfSlices);

            // Step 4: Perform word count
            JavaRDD<String> words = lines.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
            JavaRDD<Tuple2<String, Integer>> wordCounts = words
                    .mapToPair((PairFunction<String, String, Integer>) word -> new Tuple2<>(word, 1))
                    .reduceByKey(Integer::sum)
                    .map((Function<Tuple2<String, Integer>, Tuple2<String, Integer>>) t -> t);

            // Step 5: Collect and print the results
            wordCounts.collect().forEach(tuple -> {
                String message = StringUtils.upperCase(tuple._1()) + ": " + tuple._2();
                System.out.println(message);
            });

            try {
                Thread.sleep(sleepSeconds * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Step 6: Stop the Spark context
            sc.stop();
        }
    }

}