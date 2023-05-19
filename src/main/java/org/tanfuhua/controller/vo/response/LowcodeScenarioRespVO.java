package org.tanfuhua.controller.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.tanfuhua.common.constant.Constant;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author gaofubo
 * @date 2023/4/4
 */
@ApiModel("低代码SchemaVO")
@Data
public class LowcodeScenarioRespVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

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

    @JsonFormat(pattern = Constant.Str.DATE_TIME_FORMAT, timezone = Constant.Str.GMT_PLUS_8_STR)
    private LocalDateTime createTime;

    private Long createUserId;

    @JsonFormat(pattern = Constant.Str.DATE_TIME_FORMAT, timezone = Constant.Str.GMT_PLUS_8_STR)
    private LocalDateTime updateTime;

    private Long updateUserId;

    private Boolean deleteFlag;

    private Long version;

}
