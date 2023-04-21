package org.tanfuhua.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("12306登录请求体")
@Data
public class KyfwLoginReqVO {

    @ApiModelProperty("12306账号")
    private String account;

    @ApiModelProperty("12306密码")
    private String password;

}
