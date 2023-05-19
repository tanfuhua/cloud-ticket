package org.tanfuhua.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@ApiModel("低代码ScenarioVO")
@Data
public class LowcodeScenarioCreateReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "名称不能为空")
    @ApiModelProperty("name")
    private String name;

    @NotBlank(message = "路径不能为空")
    @ApiModelProperty("path")
    private String path;

    @ApiModelProperty("icon")
    private String icon;

}
