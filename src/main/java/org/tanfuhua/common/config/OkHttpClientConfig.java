package org.tanfuhua.common.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.tanfuhua.facade.KyfwFacade;
import org.tanfuhua.service.UserConfigService;
import org.tanfuhua.util.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author gaofubo
 * @date 2023/3/25
 */
@Slf4j
@Configuration
public class OkHttpClientConfig implements InitializingBean {

    @Resource
    private ProxyConfig proxyConfig;
    @Resource
    private KyfwFacade kyfwFacade;

    @Resource
    private UserConfigService userConfigService;

    private static final List<List<Proxy>> proxyListPool = new ArrayList<>();

    @Bean
    public OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient
                .Builder()
                .hostnameVerifier(SSLUtil.createTrustHostnameVerifier())
                .sslSocketFactory(SSLUtil.createSSLSocketFactory(), new SSLUtil.MiTM())
                .proxySelector(new ProxySelector() {
                    @Override
                    public List<Proxy> select(URI uri) {
                        if (CollectionUtils.isEmpty(proxyListPool)) {
                            return Collections.emptyList();
                        }
                        int i = ThreadLocalRandom.current().nextInt(proxyListPool.size());
                        List<Proxy> proxies = proxyListPool.get(i);
                        log.info("proxyServer：{},uri：{}", proxies, uri);
                        return proxies;
                    }

                    @Override
                    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                        log.error(ioe.getMessage(), ioe);
                    }
                })
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    // set-cookie逻辑
                    Headers headers = response.headers();
                    Map<String, List<String>> headerMultiMap = headers.toMultimap();
                    List<String> setCookieList = headerMultiMap.get(HttpHeaders.SET_COOKIE);
                    if (CollectionUtils.isEmpty(setCookieList)) {
                        return response;
                    }
                    log.info("RespCookie:{}", JacksonJsonUtil.toPrettyJsonString(setCookieList));
//                    List<Cookie> cookieList = setCookieList.stream().map(StringUtil::stringToCookie).collect(Collectors.toList());
//                    UserDO userDO = ContextUtil.UserHolder.getUserDOCache();
//
//                    UserConfigDO userConfigDO = userConfigService.getByUserId(userDO.getId());
//                    String cookie = userConfigDO.getCookie();
//
//                    KyfwBrowserBO browserBO = kyfwFacade.createKyfwBrowserBO(userDO.getKyfwAccount());
//                    browserBO.setCookieList(cookieList);
//                    log.info("RespCookie:{}", JacksonJsonUtil.toPrettyJsonString(FunctionUtil.convertCollToMap(cookieList, Cookie::getName, Cookie::getValue, TreeMap::new)));
                    return response;
                });
    }

    @Override
    public void afterPropertiesSet() {
        List<String> serverList = proxyConfig.getServerList();
        if (CollectionUtils.isEmpty(serverList)) {
            return;
        }
        serverList.stream()
                .map(s -> StringUtils.split(s, ":"))
                .map(strings -> new Proxy(Proxy.Type.HTTP, new InetSocketAddress(strings[0], Integer.parseInt(strings[1]))))
                .map(Lists::newArrayList)
                .forEach(proxyListPool::add);
    }
}
