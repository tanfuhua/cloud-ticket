package org.tanfuhua.cloudticket.common.interceptor;


import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.tanfuhua.cloudticket.common.constant.Constant;
import org.tanfuhua.cloudticket.exception.LoginExpireException;
import org.tanfuhua.cloudticket.util.ContextUtil;
import org.tanfuhua.cloudticket.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * Token拦截
 */
@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Value("${token.secret}")
    private String secret;
    @Value("${token.timeout-second}")
    private Long timeoutSecond;
    @Value("${token.refresh-time-second}")
    private Long refreshTimeSecond;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            log.error("Header：{}不能为空", HttpHeaders.AUTHORIZATION);
            throw new LoginExpireException();
        }
        DecodedJWT decodedJWT = JwtUtil.decodeAuthorizationWithVerify(authorization, secret);
        Long userId = decodedJWT.getClaim(Constant.Str.LOWCODE_TOKEN_USER_ID).asLong();
        ContextUtil.UserHolder.setUserId(userId);
        // 超时设置
        Date expires = decodedJWT.getExpiresAt();
        // 过期时间-当前时间 <= 30分钟
        if (expires.getTime() - System.currentTimeMillis() <= refreshTimeSecond * 1000) {
            authorization = JwtUtil.createAuthorization(userId, secret, new Date(System.currentTimeMillis() + timeoutSecond * 1000));
            response.addHeader(HttpHeaders.AUTHORIZATION, authorization);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ContextUtil.UserHolder.removeUserId();
    }

}