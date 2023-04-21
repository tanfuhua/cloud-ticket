package org.tanfuhua.controller.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @author: gaofubo
 * @date: 2021/2/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@ApiModel("12306信息")
public class KyfwInfoRespVO {
    @ApiModelProperty("用户名账号")
    private String userName;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("12306是否登录")
    private Boolean isLogin;
}
