package org.tanfuhua.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.tanfuhua.common.response.ServerResp;
import org.tanfuhua.exception.BadRequestException;


/**
 * @author: gaofubo
 * @date: 2021/2/9
 */
@ControllerAdvice
@Slf4j
public class ControllerAop {

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ServerResp<Void>> handleBadRequestException(Exception e) {
        String message = e.getMessage();
        if (StringUtils.isNotBlank(message) && message.startsWith("Invalid parameters")) {
            message = message.replace("Invalid parameters", "非法参数");
        }
        return new ResponseEntity<>(new ServerResp<>(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ServerResp<Void>> handleServerException(Throwable e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ServerResp<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
