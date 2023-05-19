package org.tanfuhua.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author gaofubo
 * @date 2023/4/13
 */
@Data
@ApiModel("低代码登录VO")
public class LowcodeLoginReqVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

}
