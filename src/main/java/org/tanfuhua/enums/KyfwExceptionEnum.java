package org.tanfuhua.enums;

import lombok.Getter;

/**
 * @author gaofubo
 * @date 2023/3/25
 */
@Getter
public enum KyfwExceptionEnum {
    BAD_COOKIE("BAD_COOKIE", "12306登录状态失效"),
    ;

    private final String code;
    private final String desc;


    KyfwExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
