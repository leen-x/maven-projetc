package com.leenx.learn.mavenproject.util;

import java.util.UUID;

/**
 * @author gongben
 * @Description:
 * @date 2021/07/31 3:39 下午
 **/
public class UUIDUtil {
    public static String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    public static String uuidUpCase() {
        return uuid().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(uuid());
        System.out.println(uuidUpCase());
    }

}
