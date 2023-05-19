package org.tanfuhua.controller.vo.response;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gaofubo
 * @date 2023/4/4
 */
@ApiModel("低代码TokenVO")
@Data
public class LowcodeAuthorizationRespVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String authorization;

}
