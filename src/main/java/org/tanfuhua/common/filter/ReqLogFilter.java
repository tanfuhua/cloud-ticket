package org.tanfuhua.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;
import static org.springframework.web.util.WebUtils.getNativeRequest;
import static org.springframework.web.util.WebUtils.getNativeResponse;

/**
 * 打印请求日志信息
 *
 * @author: gaofubo
 * @date: 2021/1/14
 */
@Slf4j
@AllArgsConstructor
@Component
@Order
public class ReqLogFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private static final int LENGTH = 1024 * 10;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        HttpServletResponse responseToUse = response;

        if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request);
        }
        if (isFirstRequest && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            if (!isAsyncStarted(requestToUse)) {
                String requestPayload = getRequestPayload(requestToUse);
                String responsePayload = getResponsePayload(responseToUse);
                updateResponse(responseToUse);
                long duration = System.currentTimeMillis() - startTime;

                log.info("ReqLog:{}", objectMapper.writeValueAsString(createRequestVO(request, response, duration,
                        requestPayload, responsePayload)));
            }
        }

    }

    protected String getRequestPayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, LENGTH);
                try {
                    return normalizeSpace(new String(buf, 0, length, wrapper.getCharacterEncoding()));
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return StringUtils.EMPTY;
    }

    protected String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, LENGTH);
                try {
                    return normalizeSpace(new String(buf, 0, length, wrapper.getCharacterEncoding()));
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return "";
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper wrapper = getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.copyBodyToResponse();
        }
    }

    private RequestBO createRequestVO(HttpServletRequest request, HttpServletResponse response, long duration,
                                      String requestPayload, String responsePayload) {
        return RequestBO.builder()
                .status(response.getStatus())
                .duration(duration)
                .uri(request.getRequestURI())
                .queryParams(request.getQueryString())
                .method(request.getMethod())
                .protocol(request.getProtocol())
                .requestIp(getRemoteAddr(request))
                .headers(ImmutableMap.of("Accept:", String.valueOf(request.getHeader("Accept")), "Content-Type:",
                        String.valueOf(request.getHeader("Content-Type"))))
                .requestBody(requestPayload)
                .responseBody(responsePayload)
                .build();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }

        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }

        if ("127.0.0.1".equals(remoteAddr) || remoteAddr.endsWith("0:0:0:0:0:0:1")) {
            try {
                remoteAddr = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ignored) {
            }
        }

        if (StringUtils.isNotBlank(remoteAddr) && remoteAddr.length() > 15 && remoteAddr.contains(",")) {
            remoteAddr = remoteAddr.substring(0, remoteAddr.indexOf(44));
        }

        return remoteAddr;
    }

    @Data
    @Builder
    private static class RequestBO implements Serializable {

        private static final long serialVersionUID = 7646097845423976197L;
        private Integer status;
        private Long duration;
        private String uri;
        private String queryParams;
        private String method;
        private String protocol;
        private Map<String, String> headers;
        private String requestIp;
        private String requestBody;
        private String responseBody;

    }

}
