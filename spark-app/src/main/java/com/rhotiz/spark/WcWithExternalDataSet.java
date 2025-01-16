package com.rhotiz.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.ZoneId;

public class WcWithExternalDataSet {
    public static void main(String[] args) throws URISyntaxException {
        String appName = WcWithExternalDataSet.class.getSimpleName() + LocalTime.now(ZoneId.of("Asia/Tehran"));
        SparkConf conf = new SparkConf().setAppName(appName)
                .setMaster(System.getenv("SPARK_MASTER_URL"));

        boolean seeCachedRdd = false;
        if (args.length == 1) {
            seeCachedRdd = Boolean.parseBoolean(args[0]);
        }

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            JavaRDD<String> words = sc.textFile("/tmp/words.txt");
            JavaPairRDD<String, Integer> wordCounts = words
                    .mapToPair(word -> new Tuple2<>(word, 1))
                    .reduceByKey(Integer::sum)
                    .persist(seeCachedRdd ? StorageLevel.MEMORY_ONLY() : StorageLevel.NONE());

            wordCounts
                    .map(t -> t)
                    .sortBy(Tuple2::_2, false, 1)
                    .collect()
                    .forEach(t -> System.out.println(t._1() + " :" + t._2()));

            ThreadHelper.sleepSec(seeCachedRdd ? 60 : 0);
            sc.stop();
        }
    }
}
