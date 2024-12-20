package com.rhotiz.container.demo.zookeeper;

import java.nio.charset.StandardCharsets;

public class ZookeeperUtil {

    public static byte[] toByteArray(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static String toString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
