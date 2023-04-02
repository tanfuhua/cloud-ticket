package org.tanfuhua.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author gaofubo
 * @date 2021/7/31
 */
@AllArgsConstructor
@Getter
@ApiModel("车票类型")
public enum KyfwPurposeCodeEnum {
    ADULT("ADULT", "成人/儿童"),
    STUDENT("0X00", "学生");

    @JsonValue
    private final String code;
    private final String desc;

    @JsonCreator
    public static KyfwPurposeCodeEnum getByCode(String code) {
        return Arrays.stream(values()).filter(e -> Objects.equals(code, e.getCode())).findAny().orElse(null);
    }

}
