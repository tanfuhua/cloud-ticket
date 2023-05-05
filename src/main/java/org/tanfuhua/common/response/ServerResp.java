package org.tanfuhua.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private int status;
    private String msg;
    private T data;

    public ServerResp(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ServerResp(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /**
     * 创建响应
     */
    public static <T> ResponseEntity<ServerResp<T>> createRespEntity(T data, HttpStatus status) {
        return new ResponseEntity<>(new ServerResp<>(RespStatusEnum.OK.getStatus(), RespStatusEnum.OK.getMsg(), data), status);
    }

    /**
     * 创建响应
     */
    public static ResponseEntity<ServerResp<Void>> createRespEntity(HttpStatus status) {
        return new ResponseEntity<>(new ServerResp<>(RespStatusEnum.OK.getStatus(), RespStatusEnum.OK.getMsg(), null), status);
    }

}
