package org.tanfuhua.lowcode.interceptor;


import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.tanfuhua.lowcode.config.JwtConfig;
import org.tanfuhua.lowcode.constant.Constant;
import org.tanfuhua.lowcode.exception.LoginExpireException;
import org.tanfuhua.lowcode.util.ContextUtil;
import org.tanfuhua.lowcode.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * Token拦截
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            log.error("Header:{}不能为空,ur:{}", HttpHeaders.AUTHORIZATION, request.getRequestURI());
            throw new LoginExpireException();
        }
        DecodedJWT decodedJWT = JwtUtil.decodeAuthorizationWithVerify(authorization, jwtConfig.getSecret());
        Long userId = decodedJWT.getClaim(Constant.Str.LOWCODE_TOKEN_USER_ID).asLong();
        ContextUtil.UserHolder.setUserId(userId);
        // 超时设置
        Date expires = decodedJWT.getExpiresAt();
        // 过期时间-当前时间 <= 30分钟
        if (expires.getTime() - System.currentTimeMillis() <= jwtConfig.getRefreshTimeSecond() * 1000) {
            authorization = JwtUtil.createAuthorization(userId, jwtConfig.getSecret(), new Date(System.currentTimeMillis() + jwtConfig.getTimeoutSecond() * 1000));
            response.addHeader(HttpHeaders.AUTHORIZATION, authorization);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ContextUtil.UserHolder.removeUserId();
    }

}