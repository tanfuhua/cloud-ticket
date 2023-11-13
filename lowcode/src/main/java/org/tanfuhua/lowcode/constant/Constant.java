package org.tanfuhua.lowcode.constant;

import java.util.TimeZone;

/**
 * @author: gaofubo
 * @date: 2021/1/22
 */
public class Constant {
    /*----------------------- 字符串 ------------------------*/
    public static class Str {
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_TIME_BROWSER_FORMAT = "EEE, dd-MMM-yyyy HH:mm:ss z";
        public static final String DATE_TIME_KYFW_FORMAT = "EEE MMM dd yyyy HH:mm:ss z (中国标准时间)";
        public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
        public static final String APPLICATION_FORM_UTF8 = "application/x-www-form-urlencoded; charset=UTF-8";
        public static final String LOWCODE_TOKEN_USER_ID = "userId";
        public static final String SPRING = "spring";
        public static final String GMT_PLUS_8_STR = "GMT+8";
    }


    /*----------------------- 时间 ------------------------*/

    public static class Time {
        public static final TimeZone GMT_PLUS_8 = TimeZone.getTimeZone(Str.GMT_PLUS_8_STR);

    }

}
