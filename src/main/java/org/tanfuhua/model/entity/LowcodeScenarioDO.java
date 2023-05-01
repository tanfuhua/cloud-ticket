package org.tanfuhua.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gaofubo
 * @date 2023/4/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("lowcode_scenario")
public class LowcodeScenarioDO extends BaseDO{

    private String name;

    @TableField("`schema`")
    private String schema;

    private String packages;

}
