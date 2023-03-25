package org.tanfuhua.common.config;


import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.tanfuhua.model.bo.KyfwBrowserBO;
import org.tanfuhua.model.bo.UserBO;
import org.tanfuhua.util.FunctionUtil;
import org.tanfuhua.util.JacksonJsonUtil;
import org.tanfuhua.util.SessionUtil;
import org.tanfuhua.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author: gaofubo
 * @date: 2021/1/22
 */
@Slf4j
@AllArgsConstructor
public class FeignConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;
    private final AppConfig appConfig;

    // new一个form编码器，实现支持form表单提交
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // referer
            String referer = appConfig.getKyfwUrlRefererMap().get(template.url());
            if (referer != null) {
                template.header("Referer", referer);
            }

            // UserBO
            UserBO userBO = SessionUtil.getUserBO();
            KyfwBrowserBO browser = userBO.getKyfwBrowserBO();
            List<Cookie> cookieList = browser.getCookieList();

            Map<String, String> cookieMap = FunctionUtil.convertCollToMap(cookieList, Cookie::getName, Cookie::getValue, TreeMap::new);

            String cookie = StringUtil.cookieListToCookieKVString(cookieList);
            log.info("ReqCookie:{}{}:{}", userBO.getKyfwAccount(), System.lineSeparator(),
                    JacksonJsonUtil.toPrettyJsonString(cookieMap));
            template.header(HttpHeaders.COOKIE, cookie);
        };
    }

    // 开启Feign的日志
    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }


}
