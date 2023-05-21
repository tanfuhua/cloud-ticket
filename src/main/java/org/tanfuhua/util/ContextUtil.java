package org.tanfuhua.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.tanfuhua.model.entity.UserDO;
import org.tanfuhua.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author gaofubo
 * @date 2023/5/13
 */
public class ContextUtil {

    public static final class RequestResponseHolder {
        public static HttpServletRequest getHttpServletRequest() {
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Objects.requireNonNull(servletRequestAttributes, "ContextUtil.RequestHolder获取HttpServletRequest为null");
            return servletRequestAttributes.getRequest();
        }

        public static HttpServletResponse getHttpServletResponse() {
            ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Objects.requireNonNull(servletRequestAttributes, "ContextUtil.RequestHolder获取HttpServletRequest为null");
            return servletRequestAttributes.getResponse();
        }
    }


    public static final class UserHolder {
        private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

        public static void setUserId(Long userId) {
            userThreadLocal.set(userId);
        }

        public static Long getUserId() {
            return userThreadLocal.get();
        }

        public static void removeUserId() {
            userThreadLocal.remove();
        }

        public static UserDO getUserDOCache() {
            Long userId = getUserId();
            return SpringUtil.getBean(UserService.class).getById(userId);
        }

    }

}
