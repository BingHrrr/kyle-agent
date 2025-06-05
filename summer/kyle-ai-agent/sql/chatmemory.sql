CREATE DATABASE IF NOT EXISTS social_agent CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE social_agent;

-- 创建对话记忆表
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE IF NOT EXISTS `chat_message`
(
    `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversation_id` varchar(64)     NOT NULL COMMENT '会话ID',
    `message_type`    varchar(20)     NOT NULL COMMENT '消息类型',
    `content`         text            NOT NULL COMMENT '消息内容',
    `metadata`        text            NOT NULL COMMENT '元数据',
    `create_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`       tinyint(1)      NOT NULL DEFAULT 0 COMMENT '是否删除 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    INDEX `idx_conversation_id` (`conversation_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='聊天消息表';


create database if not exists `social_agent` default charset utf8mb4 collate utf8mb4_unicode_ci;

use `social_agent`;

CREATE TABLE IF NOT EXISTS `conversation_memory`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `conversation_id` VARCHAR(64)     NOT NULL,
    `type`            varchar(10)     not null,
    `memory`          text            NOT NULL,
    `created_at`      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_delete`       tinyint                  default 0,
    PRIMARY KEY (`id`),
    INDEX idx_conv_prefix (conversation_id(10))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
