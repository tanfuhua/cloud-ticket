package org.tanfuhua.cloudticket.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author gaofubo
 * @date 2021/7/24
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public abstract class BaseDO {

    @TableId(type = IdType.AUTO)
    protected Long id;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    protected Long createUserId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long updateUserId;

    @TableLogic(value = "0", delval = "1")
    protected Boolean deleteFlag;

    @Version
    @TableField(update = "%s+1", fill = FieldFill.INSERT_UPDATE)
    protected Long version;

}
