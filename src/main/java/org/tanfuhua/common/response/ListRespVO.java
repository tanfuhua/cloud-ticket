package org.tanfuhua.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * ID响应VO
 *
 * @author gaofubo
 * @date 2023/5/2
 */
@ApiModel("List响应VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListRespVO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("List")
    private List<T> items;

}
