package org.tanfuhua.exception;

import lombok.Getter;
import org.tanfuhua.enums.KyfwExceptionEnum;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Getter
public class KyfwException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public KyfwException(String message) {
        super(message);
    }

    public KyfwException(KyfwExceptionEnum exceptionEnum) {
        super(exceptionEnum.getDesc());
    }

}
