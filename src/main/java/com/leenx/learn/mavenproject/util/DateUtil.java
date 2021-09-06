package com.leenx.learn.mavenproject.util;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author gongben
 * @Description:
 * @date 2021/08/03 10:24 上午
 **/
public class DateUtil {
    /**
     * 返回系统当前的完整日期时间 <br>
     * 格式 1：2008-05-02 13:12:44 <br>
     * 格式 2：2008/05/02 13:12:44 <br>
     * 格式 3：2008年5月2日 13:12:44 <br>
     * 格式 4：2008年5月2日 13时12分44秒 <br>
     * 格式 5：2008年5月2日 星期五 13:12:44 <br>
     * 格式 6：2008年5月2日 星期五 13时12分44秒 <br>
     * 格式 7：20080502 <br>
     * 格式 8：20080502131244 <br>
     * 格式 9：2008-05-02 <br>
     * 格式 10：2008_05 <br>
     * 格式 11：2008 <br>
     * 格式 12：200805 <br>
     * 格式 13：2008-05 <br>
     * 格式 default：yyyyMMddHHmmss:20080502131244 <br>
     *
     * @param formatType 格式
     * @param date       日期
     * @return String
     */
    public static String dateToString(Date date, int formatType) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern(formatType));
        sdf.setLenient(false);
        return sdf.format(date);
    }



    public static String pattern(int formatType) {
        String pattern = null;
        switch (formatType) {
            case 1:
                pattern = "yyyy-MM-dd HH:mm:ss";
                break;
            case 2:
                pattern = "yyyy/MM/dd HH:mm:ss";
                break;
            case 3:
                pattern = "yyyy年MM月dd日 HH:mm:ss";
                break;
            case 4:
                pattern = "yyyy年MM月dd日 HH时mm分ss秒";
                break;
            case 5:
                pattern = "yyyy年MM月dd日 E HH:mm:ss";
                break;
            case 6:
                pattern = "yyyy年MM月dd日 E HH时mm分ss秒";
                break;
            case 7:
                pattern = "yyyyMMdd";
                break;
            case 8:
                pattern = "yyyyMMddHHmmss";
                break;
            case 9:
                pattern = "yyyy-MM-dd";
                break;
            case 10:
                pattern = "yyyy_MM";
                break;
            case 11:
                pattern = "yyyy";
                break;
            case 12:
                pattern = "yyyyMM";
                break;
            case 13:
                pattern = "yyyy-MM";
                break;
            case 14:
                pattern = "MM-dd";
                break;
            case 15:
                pattern = "yyyy年MM月dd日";
                break;
            case 16:
                pattern = "ss";
                break;
            case 17:
                pattern = "yyyy年MM月";
                break;
            case 18:
                pattern = "yyyy/MM/dd";
                break;
            case  19:
                pattern = "MM-dd HH:mm";
                break;
            default:
                pattern = "yyyyMMddHHmmss";
        }
        return pattern;
    }

    /**
     * 字符串转日期
     *
     * @param date       日期字符串
     * @param formatType 格式
     * @return Date日期
     */
    public static Date stringToDate(String date, int formatType) {
        if (date == null || "".equals(date)) {
            return null;
        }
        SimpleDateFormat sdf = null;
        switch (formatType) {
            case 1:
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
            case 2:
                sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                break;
            case 3:
                sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                break;
            case 4:
                sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
                break;
            case 5:
                sdf = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm:ss");
                break;
            case 6:
                sdf = new SimpleDateFormat("yyyy年MM月dd日 E HH时mm分ss秒");
                break;
            case 7:
                sdf = new SimpleDateFormat("yyyyMMdd");
                break;
            case 8:
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                break;
            case 9:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case 10:
                sdf = new SimpleDateFormat("yyyy_MM");
                break;
            case 11:
                sdf = new SimpleDateFormat("yyyy");
                break;
            case 12:
                sdf = new SimpleDateFormat("yyyyMM");
                break;
            case 13:
                sdf = new SimpleDateFormat("yyyy-MM");
                break;
            case 14:
                sdf = new SimpleDateFormat("yyyy年MM月dd日");
                break;
            default:
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                break;
        }
        sdf.setLenient(false);
        try {
            return sdf.parse(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getCurrentLastTime() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);
        return today.getTime();
    }

    public static Date getCurrentBeginTime() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today.getTime();
    }

    /**
     * 日期计算
     * @param date 初始值 （目前仅支持天、小时、分钟、秒为单位的计算）
     * @param computer 计算值（正数为加，负数为减）
     * @param timeUnit 计算值单位
     * @return 计算结果， 默认返回秒的技术
     */
    public static Date dateComputer(Date date, Integer computer, TimeUnit timeUnit) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        switch (timeUnit) {
            case DAYS:
                localDateTime = localDateTime.plusDays(computer);
                break;
            case HOURS:
                localDateTime = localDateTime.plusHours(computer);
                break;
            case MINUTES:
                localDateTime = localDateTime.plusMinutes(computer);
                break;
            case SECONDS:
                localDateTime = localDateTime.plusSeconds(computer);
                break;
            default:
                localDateTime = localDateTime.plusSeconds(computer);
                break;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean equalsOrAfter(Date date1, Date date2) {
        return date1.equals(date2) || date1.after(date2);
    }
}

