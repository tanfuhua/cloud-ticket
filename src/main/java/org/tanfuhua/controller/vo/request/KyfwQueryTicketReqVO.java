package org.tanfuhua.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.tanfuhua.enums.KyfwPurposeCodeEnum;

/**
 * @author: gaofubo
 * @date: 2021/4/18
 */
@Data
@ApiModel("查询火车票余票")
public class KyfwQueryTicketReqVO {
    @ApiModelProperty("始发站code，如：BJP")
    private String fromStation;
    @ApiModelProperty("到达站code，如：SHH")
    private String toStation;
    @ApiModelProperty("日期，如：2023-03-25")
    private String trainDate;
    // 成人：ADULT，学生：0X00
    @ApiModelProperty("票类型：成人or学生,成人：ADULT，学生：0X00")
    private KyfwPurposeCodeEnum purposeCode;
}
