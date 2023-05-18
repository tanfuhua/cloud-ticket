package org.tanfuhua.common.response;

import lombok.Getter;

/**
 * @author gaofubo
 * @date 2023/5/4
 */
@Getter
public enum RespStatusEnum {

    OK(0, "OK"),
    ERROR(1000, "ERROR"),
    LOGIN_EXPIRE(1010, "请重新登录"),
    ;

    private final int status;
    private final String msg;

    RespStatusEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
