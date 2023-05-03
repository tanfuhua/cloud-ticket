ALTER TABLE lowcode_scenario
    ADD COLUMN `path`   VARCHAR(255) NULL COMMENT '页面路径' AFTER `name`,
    ADD COLUMN `icon`   VARCHAR(255) NULL COMMENT '页面图标' AFTER `path`,
    ADD COLUMN `config` TEXT         NULL COMMENT '页面配置' AFTER `icon`;