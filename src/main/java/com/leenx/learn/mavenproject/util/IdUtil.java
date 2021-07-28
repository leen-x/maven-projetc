package com.leenx.learn.mavenproject.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author leen-x
 * @Description:
 * @date 2021/07/26 10:38 上午
 **/
public class IdUtil {
    public static String STR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    public static String generateShortUuid() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            stringBuilder.append(STR.charAt(random.nextInt(STR.length())));
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        String date = "123";
        String md5 = DigestUtils.md5Hex(date);
        System.out.println(md5);
    }
}
