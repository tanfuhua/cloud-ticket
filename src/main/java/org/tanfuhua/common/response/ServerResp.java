package org.tanfuhua.common.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author: gaofubo
 * @date: 2021/2/8
 */
@Getter
@Setter
public class ServerResp<T> {
    private T data;
    private int code;
    private String message;

    public ServerResp(HttpStatus status, T data) {
        this.code = status.value();
        this.data = data;
        this.message = status.getReasonPhrase();
    }

    public ServerResp(HttpStatus status) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
    }

    public ServerResp(HttpStatus status, String message) {
        this.code = status.value();
        this.message = message;
    }

    /**
     * 创建响应
     */
    public static <T> ResponseEntity<ServerResp<T>> createRespEntity(T data, HttpStatus status) {
        return new ResponseEntity<>(new ServerResp<>(status, data), status);
    }

    /**
     * 创建响应
     */
    public static ResponseEntity<ServerResp<Void>> createRespEntity(HttpStatus status) {
        return new ResponseEntity<>(new ServerResp<>(status), status);
    }

}
