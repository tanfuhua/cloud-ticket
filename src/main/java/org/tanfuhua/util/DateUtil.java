package org.tanfuhua.util;


import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author: gaofubo
 * @date: 2020/9/2
 */
@UtilityClass
public class DateUtil {

    /**
     * 根据日期格式获取时间字符串
     */
    public static String dateToString(Date date, String formatPattern, Locale locale, TimeZone zone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern, locale);
        simpleDateFormat.setTimeZone(zone);
        return simpleDateFormat.format(date);
    }


    /**
     * 根据日期格式，解析时间字符串
     */
    public static Date stringToDate(String dateTimeString, String formatPattern) {
        if (StringUtils.isBlank(dateTimeString)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        try {
            return simpleDateFormat.parse(dateTimeString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据日期格式，解析时间字符串
     */
    public static Date stringToDate(String dateTimeString, String formatPattern, Locale locale) {
        if (StringUtils.isBlank(dateTimeString)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern, locale);
        try {
            return simpleDateFormat.parse(dateTimeString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
