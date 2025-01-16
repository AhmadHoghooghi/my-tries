package com.rhotiz.spark;

public class ThreadHelper {

    public static void sleepSec(int sec) {
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
