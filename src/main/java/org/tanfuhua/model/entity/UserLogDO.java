package org.tanfuhua.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户日志实体类
 *
 * @author: gaofubo
 * @date: 2021/2/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_log")
public class UserLogDO extends BaseDO {

    private String log;

}
