package org.tanfuhua.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author gaofubo
 * @date 2021/7/25
 */
@Data
@EqualsAndHashCode
public class UserBO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;

    private String userName;

    private String realName;

    private String kyfwAccount;

    private String kyfwPassword;

    private KyfwBrowserBO kyfwBrowserBO;
}
