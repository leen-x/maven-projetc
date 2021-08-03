package com.leenx.learn.mavenproject.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author gongben
 * @Description: JDK8 时间工具类
 * @date 2021/08/02 8:14 下午
 **/
public class TimeUtil {
    private static final ZoneId SH_ZONE_ID = ZoneId.of("Asia/Shanghai");

    private static String format_1 = ""

    /**
     * convert java.util.Date 2 java.time.LocalDateTime
     * @param date java.util.Date
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime convertDate2Time(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now();
    }
}
