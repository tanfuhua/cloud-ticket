package org.tanfuhua.model.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.tanfuhua.common.json.jsondeserialize.BooleanJsonDeserialize;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息
 *
 * @author: gaofubo
 * @date: 2021/1/30
 */
@Data
public class KyfwUserBO implements Serializable {
    private static final long serialVersionUID = 7488300053040617381L;
    @JsonAlias("isstudentDate")
    private Boolean isStudentDate;
    @JsonAlias("is_message_passCode")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isMessagePassCode;
    @JsonAlias("born_date")
    private String bormDate;
    @JsonAlias("is_phone_check")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isPhoneCheck;
    @JsonAlias("studentDate")
    private List<String> studentDate;
    @JsonAlias("is_uam_login")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isUamLogin;
    @JsonAlias("is_login_passCode")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isLoginPassCode;
    @JsonAlias("is_sweep_login")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isSweepLogin;
    @JsonAlias("queryUrl")
    private String queryUrl;
    @JsonAlias("psr_qr_code_result")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean psrQrCodeResult;
    @JsonAlias("now")
    private Long now;
    @JsonAlias("user_name")
    private String userName;
    @JsonAlias("name")
    private String name;
    @JsonAlias("is_login")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isLogin;
    @JsonAlias("is_olympicLogin")
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isOlympicLogin;
}
