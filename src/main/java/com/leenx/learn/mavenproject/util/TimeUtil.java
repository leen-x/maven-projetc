package com.leenx.learn.mavenproject.util;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * @author gongben
 * @Description: JDK8 时间工具类
 * @date 2021/08/02 8:14 下午
 **/
public class TimeUtil {
    private static final ZoneId SH_ZONE_ID = ZoneId.of("Asia/Shanghai");

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

    /**
     * 获取的年、月、日（时间戳：秒级别）
     *
     * @return Triple<年, 月, 日>
     */
    public static Triple<Integer, Integer, Integer> getDateYearMonthDay(Date date) {
        date.getMonth();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        // 取月份要加1
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new ImmutableTriple<>(year, month, day);
    }


    /**
     * 返回时间差
     *
     * @param a
     * @param b
     * @return a < b 则为负数
     */
    public static long diff(Date a, Date b) {
        return a.getTime() - b.getTime();
    }

    /**
     * 返回时间差
     *
     * @param a
     * @param b
     * @return
     */
    public static long diffAbs(Date a, Date b) {
        return Math.abs(a.getTime() - b.getTime());
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now();
    }
}
