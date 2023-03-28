package org.tanfuhua.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.tanfuhua.common.constant.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: gaofubo
 * @date: 2021/4/24
 */
@Slf4j
@UtilityClass
public class StringUtil {

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static Cookie stringToCookie(String cookieKVString) {
        String[] cookieArr = cookieKVString.split(";");
        if (cookieArr.length <= 1) {
            String[] kv = cookieArr[0].split("=");
            return new Cookie(kv[0].trim(), kv[1].trim());
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < cookieArr.length; i++) {
            String[] kv = cookieArr[i].split("=");
            if (i == 0) {
                map.put("name", kv[0].trim());
                map.put("value", kv[1].trim());
                continue;
            }
            if (kv.length <= 1) {
                map.put(kv[0].trim().toLowerCase(), null);
                continue;
            }
            map.put(kv[0].trim().toLowerCase(), kv[1].trim());
        }
        return new Cookie(
                map.get("name"),
                map.get("value"),
                map.get("domain"),
                map.get("path"),
                DateUtil.stringToDate(map.getOrDefault("expires", ""), Constant.Str.DATE_TIME_BROWSER_FORMAT, Locale.ENGLISH),
                map.containsKey("secure"));
    }


    public static String cookieListToKVString(List<Cookie> cookieList) {
        return cookieList
                .stream()
                .map(cookie -> String.format("%s=%s", cookie.getName(), cookie.getValue()))
                .collect(Collectors.joining("; "));
    }

}
