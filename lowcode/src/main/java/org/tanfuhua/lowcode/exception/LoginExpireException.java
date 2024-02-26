package org.tanfuhua.lowcode.exception;

import lombok.Getter;
import lombok.Setter;
import org.tanfuhua.lowcode.constant.RespStatusEnum;

/**
 * @author: gaofubo
 * @date: 2021/2/8
 */
@Setter
@Getter
public class LoginExpireException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LoginExpireException() {
        super(RespStatusEnum.LOGIN_EXPIRE.getMsg());
    }
}
