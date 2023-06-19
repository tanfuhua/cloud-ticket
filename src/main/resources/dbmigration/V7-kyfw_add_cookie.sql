ALTER TABLE user_config
    ADD COLUMN `cookie` TEXT NULL COMMENT 'kyfw的cookie' AFTER `cookie_valid_status`;