package org.tanfuhua.cloudticket.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 抢订模式
 *
 * @author: gaofubo
 * @date: 2021/3/5
 */
@Getter
public enum BookTypeEnum {
    INITIAL(0, "初始值"),
    PERIOD(1, "捡漏抢"),
    SCHEDULED(2, "预约抢");

    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    BookTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator
    public static BookTypeEnum getEnumByValue(int value) {
        return Arrays.stream(values()).filter(e -> Objects.equals(e.getValue(), value)).findAny().orElse(null);
    }
}
