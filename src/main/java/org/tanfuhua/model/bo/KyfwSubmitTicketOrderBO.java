package org.tanfuhua.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.tanfuhua.enums.KyfwPurposeCodeEnum;

/**
 * 预定前请求
 *
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Data
public class KyfwSubmitTicketOrderBO {

    /**
     * 车票秘钥，如："secretStr": "VRBMq7JbEL%2Bl40v%2BZvu7oydN9q6Vv5BT%2FyZd1T9hicJ%2BOQQPwdNU2BbHxeRMxM5VZaa13akB%2B3rR%0ADLpNCnjKCHPvDRxKotxfjzGtrtxoZ8ZzaKzz%2FYpydZnW9xmuVyrKYBrQVzOLQIE5ahEoB0euJem7%0Agt4EY%2BcSynQv%2FKgkRJHu9wNDM1LIuqg%2B9E5ANGQmGN4LWSDpy7PMw96jn87q1jpwy%2BVvaqSkh%2Fdy%0Abg5qS%2FaQTykAp0Qs6%2Bc1Ld8Rl7kobeIhqYf3v0BMHT%2FYtty3jyDdxJSFWYQFHHQG8p7wZjq91mAl%0AGOrVlnVtqmJkJjAd"
     */
    private String secretStr;

    /**
     * 车票日期
     */
    @JsonProperty("train_date")
    private String trainDate;

    /**
     * 返程日期
     */
    @JsonProperty("back_train_date")
    private String backTrainDate;

    /**
     * 旅游标志，如：dc
     */
    @JsonProperty("tour_flag")
    private String tourFlag = "dc";

    /**
     * 车票类型，如：ADULT-普通/儿童，0X00-学生
     */
    @JsonProperty("purpose_codes")
    private KyfwPurposeCodeEnum purposeCodes;

    /**
     * 出发地名，如："fromStationName": "北京南"
     */
    @JsonProperty("query_from_station_name")
    private String queryFromStationName;

    /**
     * 到达地名，如："toStationName": "上海虹桥"
     */
    @JsonProperty("query_to_station_name")
    private String queryToStationName;
}
