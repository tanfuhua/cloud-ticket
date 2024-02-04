USE lowcode;

CREATE TABLE `lowcode_scenario`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`           varchar(255) NOT NULL COMMENT 'name',
    `path`           varchar(255) DEFAULT NULL COMMENT '页面路径',
    `icon`           varchar(255) DEFAULT NULL COMMENT '页面图标',
    `config`         text COMMENT '页面配置',
    `schema`         text COMMENT 'schema',
    `create_user_id` bigint       NOT NULL COMMENT ' 创建人',
    `create_time`    datetime     NOT NULL COMMENT '创建时间',
    `update_user_id` bigint       DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
    `delete_flag`    tinyint      DEFAULT '0' COMMENT '是否删除0-否 1-是',
    `version`        bigint       DEFAULT '0' COMMENT '版本号',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='低代码页面配置表';

CREATE TABLE `lowcode_user`
(
    `id`             int         NOT NULL AUTO_INCREMENT,
    `username`       varchar(50) NOT NULL COMMENT '账号',
    `password`       varchar(50) NOT NULL COMMENT '密码',
    `role`           tinyint     NOT NULL COMMENT '角色：0-普通用户、1-管理员',
    `permission`     text     DEFAULT NULL COMMENT '权限：scenarioID列表，多个逗号分割',
    `create_user_id` bigint      NOT NULL COMMENT ' 创建人',
    `create_time`    datetime    NOT NULL COMMENT '创建时间',
    `update_user_id` bigint   DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime DEFAULT NULL COMMENT '更新时间',
    `delete_flag`    tinyint  DEFAULT '0' COMMENT '是否删除0-否 1-是',
    `version`        bigint   DEFAULT '0' COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `i_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='低代码用户表';

