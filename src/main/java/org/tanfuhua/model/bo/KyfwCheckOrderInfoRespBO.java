package org.tanfuhua.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.tanfuhua.common.json.jsondeserialize.BooleanJsonDeserialize;

/**
 * @author: gaofubo
 * @date: 2021/4/22
 */
@Data
public class KyfwCheckOrderInfoRespBO {
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean canChooseBeds;
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean canChooseSeats;
    /**
     * 如：M
     */
    @JsonProperty("choose_Seats")
    private String chooseSeats;
    @JsonDeserialize(using = BooleanJsonDeserialize.class)
    private Boolean isCanChooseMid;
    private String ifShowPassCodeTime;
    private Boolean submitStatus;
    private String smokeStr;
    private String errMsg;
}
