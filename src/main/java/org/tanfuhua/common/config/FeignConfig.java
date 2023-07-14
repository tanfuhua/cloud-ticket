package org.tanfuhua.common.config;


import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.tanfuhua.facade.KyfwFacade;
import org.tanfuhua.model.bo.KyfwBrowserBO;
import org.tanfuhua.model.entity.UserConfigDO;
import org.tanfuhua.model.entity.UserDO;
import org.tanfuhua.service.UserConfigService;
import org.tanfuhua.service.UserService;
import org.tanfuhua.util.ContextUtil;
import org.tanfuhua.util.FunctionUtil;
import org.tanfuhua.util.JacksonJsonUtil;
import org.tanfuhua.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author: gaofubo
 * @date: 2021/1/22
 */
@Slf4j
@AllArgsConstructor
public class FeignConfig {

    private final ObjectFactory<HttpMessageConverters> messageConverters;
    @Resource
    private AppConfig appConfig;
    @Resource
    private UserConfigService userConfigService;
    @Resource
    private KyfwFacade kyfwFacade;

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
            String referer = appConfig.getKyfw().getUrlRefererMap().get(template.url().replace("/", ""));
            if (referer != null) {
                template.header("Referer", referer);
            }

            // UserBO
            UserDO userDO = ContextUtil.UserHolder.getUserDOCache();
            UserConfigDO userConfigDO = userConfigService.getByUserId(userDO.getId());
            template.header(HttpHeaders.COOKIE, userConfigDO.getCookie());
        };
    }

    // 开启Feign的日志
    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }


}
