package org.tanfuhua.model.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.tanfuhua.exception.KyfwException;

import java.util.Objects;

/**
 * 12306响应格式
 *
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Data
public class KyfwRespBO<T> {
    private T data;
    @JsonAlias("httpstatus")
    private Integer httpStatus;
    private Object messages;
    private Boolean status;
    private Object validateMessages;
    private String validateMessagesShowId;

    public boolean checkResponseStatusAndReturn() {
        return getStatus() && Objects.equals(getHttpStatus(), HttpStatus.OK.value());
    }

    public void checkResponseStatus() throws KyfwException {
        if (checkResponseStatusAndReturn()) {
            return;
        }
        throw new KyfwException(String.valueOf(messages));
    }

    public static boolean isHtml(String response) {
        return response.contains("DOCTYPE html");
    }
}
