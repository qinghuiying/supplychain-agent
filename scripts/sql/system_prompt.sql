CREATE TABLE IF NOT EXISTS `sys_system_prompt` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `agent_code` VARCHAR(64) NOT NULL COMMENT 'agent 标识',
  `env` VARCHAR(32) NOT NULL COMMENT '环境: dev/test/uat/prod',
  `prompt_content` TEXT NOT NULL COMMENT '系统提示词',
  `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
  `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '1 生效, 0 失效',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_agent_env_enabled` (`agent_code`, `env`, `enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统提示词配置表';

INSERT INTO `sys_system_prompt` (`agent_code`, `env`, `prompt_content`, `version`, `enabled`)
VALUES ('default-agent', 'dev', 'You are a helpful supply chain assistant in DEV.', 1, 1);

-- update example:
-- UPDATE `sys_system_prompt`
-- SET `prompt_content` = 'You are a strict supply chain assistant.',
--     `version` = 2,
--     `enabled` = 1
-- WHERE `id` = 1;
