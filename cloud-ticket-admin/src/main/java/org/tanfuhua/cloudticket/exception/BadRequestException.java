package org.tanfuhua.cloudticket.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: gaofubo
 * @date: 2021/2/8
 */
@Setter
@Getter
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(Throwable e) {
        super(e.getMessage());
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String format, Object... args) {
        super(String.format(format, args));
    }
}
