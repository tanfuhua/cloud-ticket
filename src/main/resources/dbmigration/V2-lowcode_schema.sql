CREATE TABLE `lowcode_schema`
(
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',

    `name`           varchar(255) NOT NULL COMMENT 'name',
    `schema`         text         NULL COMMENT 'schema',

    `create_user_id` bigint(20)   NOT NULL COMMENT ' 创建人',
    `create_time`    datetime     NOT NULL COMMENT '创建时间',
    `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime   DEFAULT NULL COMMENT '更新时间',
    `delete_flag`    tinyint(2) DEFAULT '0' COMMENT '是否删除0-否 1-是',
    `version`        bigint(20) DEFAULT '0' COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='低代码的schema表';