package org.tanfuhua.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author gaofubo
 * @date 2023/3/26
 */
@Getter
public enum KyfwSeatTypeEnum {
    SWZ("9", "商务座"),
    TZ("P", "特等座"),
    ZY("M", "一等座"),
    ZE("O", "二等座"),
    GR("6", "高级软卧"),
    RW("4", "软卧"),
    SRRB("F", "动卧"),
    YW("3", "硬卧"),
    RZ("2", "软座"),
    YZ("1", "硬座"),
    WZ("1", "无座"),
    QT("H", "其他"),
    ;
    @JsonValue
    private final String code;
    private final String desc;

    KyfwSeatTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static KyfwSeatTypeEnum getByCode(String code) {
        return Arrays.stream(values()).filter(e -> Objects.equals(e.code, code)).findAny().orElseThrow(() -> new RuntimeException("未知的code:" + code));
    }
}
