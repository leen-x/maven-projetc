package com.leenx.learn.mavenproject;

import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * @author leen-x
 * @Description: 时间工具类，默认的时区为操作系统的时区
 * @date 2021/09/01 2:41 下午
 **/
public class DateUtil {
    /**
     * LocalDateTime转Date
     *
     * @param localDateTime
     * @return
     */
    public static Date convertLocalDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime convertDate2LocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 获取指定月份的开始结束时间
     *
     * @param year  >= 0
     * @param month [1, 12]
     * @return util.Date
     */
    public static Optional<Pair<Date, Date>> getMonthStartEndDate(int year, int month) {
        if (year < 0 || month <= 0 || month > 12) {
            return Optional.empty();
        }
        // YearMon
        YearMonth yearMonth = YearMonth.of(year, month);

        // YearMon开始
        LocalDate startDate = yearMonth.atDay(1);
        LocalDateTime startLocalDateTime = startDate.atTime(LocalTime.MIN);
        Date start = convertLocalDateTime2Date(startLocalDateTime);

        // YearMon结束
        LocalDate endDate = yearMonth.atEndOfMonth();
        LocalDateTime endLocalDateTime = endDate.atTime(LocalTime.MAX);
        Date end = convertLocalDateTime2Date(endLocalDateTime);

        return Optional.of(Pair.of(start, end));
    }
}
