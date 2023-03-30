package org.tanfuhua.exception;

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
}
