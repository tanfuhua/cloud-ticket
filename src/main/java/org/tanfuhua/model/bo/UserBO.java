package org.tanfuhua.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gaofubo
 * @date 2021/7/25
 */
@Data
@EqualsAndHashCode
public class UserBO {

    private Long id;

    private String userName;

    private String realName;

    private String kyfwAccount;

    private String kyfwPassword;

    private KyfwBrowserBO kyfwBrowserBO;
}
