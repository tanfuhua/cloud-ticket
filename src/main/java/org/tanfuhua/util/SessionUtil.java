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
import java.util.Objects;
import java.util.Optional;

/**
 * @author: gaofubo
 * @date: 2021/2/9
 */
@UtilityClass
public class SessionUtil {

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
