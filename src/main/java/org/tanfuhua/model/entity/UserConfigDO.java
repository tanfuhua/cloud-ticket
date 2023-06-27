package org.tanfuhua.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.tanfuhua.enums.BookTypeEnum;

import java.util.Date;

/**
 * @author: gaofubo
 * @date: 2021/4/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_config")
public class UserConfigDO extends BaseDO {

    private Long userId;

    private String startBookInfo;

    private Boolean cookieValidStatus;

    private String cookie;

    private Date scheduleTime;

    private Boolean runStatus;

    private BookTypeEnum bookType;

    private Integer userAgentIndex;

}
