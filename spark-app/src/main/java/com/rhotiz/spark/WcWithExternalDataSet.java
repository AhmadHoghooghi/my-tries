package com.rhotiz.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.ZoneId;

public class WcWithExternalDataSet {
    public static void main(String[] args) throws URISyntaxException {
        String appName = WcWithExternalDataSet.class.getSimpleName() + LocalTime.now(ZoneId.of("Asia/Tehran"));
        SparkConf conf = new SparkConf().setAppName(appName)
                .setMaster(System.getenv("SPARK_MASTER_URL"));
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            JavaRDD<String> words = sc.textFile("/tmp/words.txt");
            words.mapToPair(word -> new Tuple2<>(word, 1))
                    .reduceByKey(Integer::sum)
                    .map(t->t)
                    .sortBy(Tuple2::_2, false, 1)
                    .collect()
                    .forEach(t -> System.out.println(t._1() + " :" + t._2()));

            sc.stop();
        }
    }
}
