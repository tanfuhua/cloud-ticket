CREATE TABLE `user`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',

    `user_name`      varchar(255) NOT NULL COMMENT '用户名',
    `real_name`      varchar(255) NOT NULL COMMENT '用户名',
    `kyfw_account`   varchar(255) NOT NULL COMMENT '12306账号',
    `kyfw_password`  varchar(255) NOT NULL COMMENT '12306密码',

    `create_user_id` bigint(20) NOT NULL COMMENT ' 创建人',
    `create_time`    datetime     NOT NULL COMMENT '创建时间',
    `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime DEFAULT NULL COMMENT '更新时间',
    `delete_flag`    tinyint(2) DEFAULT '0' COMMENT '是否删除0-否 1-是',
    `version`        bigint(20) DEFAULT '0' COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';


CREATE TABLE `user_config`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',

    `user_id`           bigint(20) NOT NULL COMMENT '用户ID',
    `startBookInfo`     text     NOT NULL COMMENT '用户名',
    `cookieValidStatus` tinyint(1) NOT NULL COMMENT '订票信息',
    `scheduleTime`      datetime NOT NULL COMMENT '调度时间',
    `runStatus`         tinyint(1) NOT NULL COMMENT '运行状态',
    `bookType`          tinyint  NOT NULL COMMENT '抢订模式',
    `userAgentIndex`    tinyint  NOT NULL COMMENT 'agent索引',

    `create_user_id`    bigint(20) NOT NULL COMMENT ' 创建人',
    `create_time`       datetime NOT NULL COMMENT '创建时间',
    `update_user_id`    bigint(20) DEFAULT NULL COMMENT '更新人',
    `update_time`       datetime DEFAULT NULL COMMENT '更新时间',
    `delete_flag`       tinyint(2) DEFAULT '0' COMMENT '是否删除0-否 1-是',
    `version`           bigint(20) DEFAULT '0' COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户配置表';

CREATE TABLE `user_log`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',

    `log`            text     NOT NULL COMMENT '日志',

    `create_user_id` bigint(20) NOT NULL COMMENT ' 创建人',
    `create_time`    datetime NOT NULL COMMENT '创建时间',
    `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime DEFAULT NULL COMMENT '更新时间',
    `delete_flag`    tinyint(2) DEFAULT '0' COMMENT '是否删除0-否 1-是',
    `version`        bigint(20) DEFAULT '0' COMMENT '版本号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='日志表';

