package org.tanfuhua.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author gaofubo
 * @date 2023/4/13
 */
@Data
@EqualsAndHashCode
public class LowcodeUserBO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String password;

}
