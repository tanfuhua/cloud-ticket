package org.tanfuhua.controller.vo.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Data
@ApiModel("乘客信息")
public class KyfwPassengerRespVO {
    private String passengerName;
    private String sexCode;
    private String sexName;
    private String bornDate;
    private String countryCode;
    private String passengerIdTypeCode;
    private String passengerIdTypeName;
    private String passengerIdNo;
    private String passengerType;
    private String passengerFlag;
    private String passengerTypeName;
    private String mobileNo;
    private String phoneNo;
    private String email;
    private String address;
    private String postalCode;
    private String firstLetter;
    private String recordCount;
    private String totalTimes;
    private String indexId;
    private String allEncStr;
    private String isAdult;
    private String isYongThan10;
    private String isYongThan14;
    private String isOldThan60;
    private String ifReceive;
    private String isActive;
    private String isBuyTicket;
    private String lastTime;
    private String mobileCheckTime;
    private String emailActiveTime;
    private String lastUpdateTime;
    private String passengerUuid;
    private String gatBornDate;
    private String gatValidDateStart;
    private String gatValidDateEnd;
    private String gatVersion;
}
