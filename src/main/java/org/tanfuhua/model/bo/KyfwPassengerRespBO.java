package org.tanfuhua.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Data
public class KyfwPassengerRespBO {
    @JsonProperty("passenger_name")
    private String passengerName;
    @JsonProperty("sex_code")
    private String sexCode;
    @JsonProperty("sex_name")
    private String sexName;
    @JsonProperty("born_date")
    private String bornDate;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("passenger_id_type_code")
    private String passengerIdTypeCode;
    @JsonProperty("passenger_id_type_name")
    private String passengerIdTypeName;
    @JsonProperty("passenger_id_no")
    private String passengerIdNo;
    @JsonProperty("passenger_type")
    private String passengerType;
    @JsonProperty("passenger_flag")
    private String passengerFlag;
    @JsonProperty("passenger_type_name")
    private String passengerTypeName;
    @JsonProperty("mobile_no")
    private String mobileNo;
    @JsonProperty("phone_no")
    private String phoneNo;
    @JsonProperty("email")
    private String email;
    @JsonProperty("address")
    private String address;
    @JsonProperty("postalcode")
    private String postalCode;
    @JsonProperty("first_letter")
    private String firstLetter;
    @JsonProperty("recordCount")
    private String recordCount;
    @JsonProperty("total_times")
    private String totalTimes;
    @JsonProperty("index_id")
    private String indexId;
    @JsonProperty("allEncStr")
    private String allEncStr;
    @JsonProperty("isAdult")
    private String isAdult;
    @JsonProperty("isYongThan10")
    private String isYongThan10;
    @JsonProperty("isYongThan14")
    private String isYongThan14;
    @JsonProperty("isOldThan60")
    private String isOldThan60;
    @JsonProperty("if_receive")
    private String ifReceive;
    @JsonProperty("is_active")
    private String isActive;
    @JsonProperty("is_buy_ticket")
    private String isBuyTicket;
    @JsonProperty("last_time")
    private String lastTime;
    @JsonProperty("mobile_check_time")
    private String mobileCheckTime;
    @JsonProperty("email_active_time")
    private String emailActiveTime;
    @JsonProperty("last_update_time")
    private String lastUpdateTime;
    @JsonProperty("passenger_uuid")
    private String passengerUuid;
    @JsonProperty("gat_born_date")
    private String gatBornDate;
    @JsonProperty("gat_valid_date_start")
    private String gatValidDateStart;
    @JsonProperty("gat_valid_date_end")
    private String gatValidDateEnd;
    @JsonProperty("gat_version")
    private String gatVersion;
}
