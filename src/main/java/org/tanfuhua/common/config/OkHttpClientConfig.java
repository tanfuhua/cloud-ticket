package org.tanfuhua.common.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.tanfuhua.model.bo.UserBO;
import org.tanfuhua.util.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author gaofubo
 * @date 2023/3/25
 */
@Slf4j
@Configuration
public class OkHttpClientConfig {

    @Bean
    public OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient
                .Builder()
                .hostnameVerifier(SSLUtil.createTrustHostnameVerifier())
                .sslSocketFactory(SSLUtil.createSSLSocketFactory(), new SSLUtil.MiTM())
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    // set-cookie逻辑
                    Optional<UserBO> userSoOptional = SessionUtil.getUserBOOptional();
                    if (!userSoOptional.isPresent()) {
                        return response;
                    }
                    Headers headers = response.headers();
                    Map<String, List<String>> headerMultiMap = headers.toMultimap();
                    List<String> setCookieList = headerMultiMap.get(HttpHeaders.SET_COOKIE);
                    if (CollectionUtils.isEmpty(setCookieList)) {
                        return response;
                    }
                    log.info("RespCookie:{}", JacksonJsonUtil.toPrettyJsonString(setCookieList));
                    List<Cookie> cookieList = setCookieList.stream().map(StringUtil::stringToCookie).collect(Collectors.toList());
                    SessionUtil.getUserBO().getKyfwBrowserBO().setCookieList(cookieList);
                    log.info("RespCookie:{}", JacksonJsonUtil.toPrettyJsonString(FunctionUtil.convertCollToMap(cookieList, Cookie::getName, Cookie::getValue, TreeMap::new)));
                    return response;
                });
    }
}
