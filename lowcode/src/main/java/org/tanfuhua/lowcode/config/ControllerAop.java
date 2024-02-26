package org.tanfuhua.lowcode.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.tanfuhua.lowcode.constant.RespStatusEnum;
import org.tanfuhua.lowcode.controller.response.ServerResp;
import org.tanfuhua.lowcode.exception.BadRequestException;
import org.tanfuhua.lowcode.exception.LoginExpireException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * @author: gaofubo
 * @date: 2021/2/9
 */
@ControllerAdvice
@Slf4j
public class ControllerAop {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ServerResp<Void>> handleBadRequestException(Exception e) {
        String message = e.getMessage();
        log.error(message);
        return new ResponseEntity<>(new ServerResp<>(RespStatusEnum.ERROR.getStatus(), message), HttpStatus.OK);
    }

    @ExceptionHandler({LoginExpireException.class})
    public ResponseEntity<ServerResp<Void>> handleLoginExpireException(Exception e) {
        String message = StringUtils.isBlank(e.getMessage()) ? RespStatusEnum.LOGIN_EXPIRE.getMsg() : e.getMessage();
        log.error(message);
        return new ResponseEntity<>(new ServerResp<>(RespStatusEnum.LOGIN_EXPIRE.getStatus(), message), HttpStatus.OK);
    }

    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ServerResp<Void>> handleArgumentException(Exception e) {
        String message = RespStatusEnum.ERROR.getMsg();
        if (e instanceof IllegalArgumentException) {
            IllegalArgumentException illegalArgumentException = (IllegalArgumentException) e;
            message = illegalArgumentException.getMessage().replace("Invalid parameters", "非法参数");
        } else if (e instanceof MethodArgumentNotValidException) {
            // @Valid异常处理
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            message = methodArgumentNotValidException
                    .getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .findAny()
                    .map(FieldError::getDefaultMessage)
                    .orElse(RespStatusEnum.ERROR.getMsg());
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) e;
            message = constraintViolationException
                    .getConstraintViolations()
                    .stream()
                    .findAny()
                    .map(ConstraintViolation::getMessage)
                    .orElse(RespStatusEnum.ERROR.getMsg());
        }
        log.error(message);
        return new ResponseEntity<>(new ServerResp<>(RespStatusEnum.ERROR.getStatus(), message), HttpStatus.OK);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ServerResp<Void>> handleServerException(Throwable e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ServerResp<>(RespStatusEnum.ERROR.getStatus(), e.getMessage()), HttpStatus.OK);
    }

}
