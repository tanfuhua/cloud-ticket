package org.tanfuhua.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * ID响应VO
 *
 * @author gaofubo
 * @date 2023/5/2
 */
@ApiModel("ID响应VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdRespVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long id;

}
