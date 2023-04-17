package org.tanfuhua.util;

import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.tanfuhua.common.constant.Constant;
import org.tanfuhua.exception.BadRequestException;
import org.tanfuhua.model.bo.UserBO;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: gaofubo
 * @date: 2021/2/9
 */
@UtilityClass
public class SessionUtil {

    /**
     * 获取HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(servletRequestAttributes, "SessionUtil获取ServletRequestAttributes为NULL！");
        return servletRequestAttributes.getRequest();
    }

    public static void setUserBO(UserBO userBO) {
        HttpServletRequest request = getHttpServletRequest();
        request.getSession().setAttribute(Constant.Str.SESSION_USER, userBO);
    }

    public static <T extends Serializable> void set(String key, T val) {
        HttpServletRequest request = getHttpServletRequest();
        request.getSession().setAttribute(key, val);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Optional<T> getOptional(String key, Class<T> tClass) {
        HttpServletRequest request = getHttpServletRequest();
        Object val = request.getSession().getAttribute(key);
        return Optional.ofNullable((T) val);
    }

    public static <T> T get(String key, Class<T> tClass) {
        return getOptional(key, tClass).orElseThrow(() -> new BadRequestException("请重新登录！"));
    }


    public static UserBO getUserBO() {
        return getUserBOOptional().orElseThrow(() -> new BadRequestException("请重新登录！"));
    }

    public static Optional<UserBO> getUserBOOptional() {
        HttpServletRequest request = getHttpServletRequest();
        Object userBO = request.getSession().getAttribute(Constant.Str.SESSION_USER);
        return Optional.ofNullable((UserBO) userBO);
    }

    public static void mockSession(UserBO userBO) {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(Constant.Str.SESSION_USER, userBO);
        mockHttpServletRequest.setSession(mockHttpSession);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));
    }
}
