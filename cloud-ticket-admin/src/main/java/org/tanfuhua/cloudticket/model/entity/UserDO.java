package org.tanfuhua.cloudticket.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: gaofubo
 * @date: 2021/5/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
public class UserDO extends BaseDO {

    private String userName;

    private String realName;

    private String kyfwAccount;

    private String kyfwPassword;


}
