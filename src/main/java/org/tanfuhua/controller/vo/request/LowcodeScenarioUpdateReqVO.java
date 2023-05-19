package org.tanfuhua.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("低代码ScenarioVO")
@Data
public class LowcodeScenarioUpdateReqVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("path")
    private String path;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("config")
    private String config;

    @ApiModelProperty("schema")
    private String schema;

}
