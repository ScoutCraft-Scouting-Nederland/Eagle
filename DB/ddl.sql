DROP TABLE IF EXISTS `permission_role`;
DROP TABLE IF EXISTS `doctrine_migration_versions`;
DROP TABLE IF EXISTS `permission`;
DROP TABLE IF EXISTS `player_note`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `help_ops`;
DROP TABLE IF EXISTS `report`;
DROP TABLE IF EXISTS `bug`;
DROP TABLE IF EXISTS `ignored`;
DROP TABLE IF EXISTS `discord_user`;
DROP TABLE IF EXISTS `chat`;
DROP TABLE IF EXISTS `session`;
DROP TABLE IF EXISTS `server_in_group`;
DROP TABLE IF EXISTS `servergroup`;
DROP TABLE IF EXISTS `server`;
DROP TABLE IF EXISTS `player`;

--
-- Table structure for table `player`
--
CREATE TABLE `player` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `display_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `notes` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `locale` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `online` tinyint(1) NOT NULL,
  `social_spy` tinyint(1) NOT NULL,
  `vanished` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `server`
--
CREATE TABLE `server` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tps` decimal(4,2) DEFAULT NULL,
  `display_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip_address` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `port` int(5) NOT NULL,
  `state` enum('offline','online','disabled') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `servergroup`
--
CREATE TABLE `servergroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `server_in_group`
--
CREATE TABLE `server_in_group` (
  `server_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`server_id`, `group_id`),
  CONSTRAINT `FK_server_in_group_server` FOREIGN KEY (`server_id`) REFERENCES `server` (`id`),
  CONSTRAINT `FK_server_in_group_group` FOREIGN KEY (`group_id`) REFERENCES `servergroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `session`
--
CREATE TABLE `session` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `player_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `started_at` datetime NOT NULL,
  `ended_at` datetime DEFAULT NULL,
  `ip_address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` int(11) DEFAULT NULL,
  `player_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `application_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_FK_session_player` (`player_id`),
  CONSTRAINT `FK_session_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `chat`
--
CREATE TABLE `chat` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `player_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `server_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `timestamp` datetime NOT NULL,
  `message` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `player_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_FK_chat_player` (`player_id`),
  KEY `IDX_FK_chat_server` (`server_id`),
  CONSTRAINT `FK_chat_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK_chat_server` FOREIGN KEY (`server_id`) REFERENCES `server` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `discord_user`
--
CREATE TABLE `discord_user` (
  `player_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `discord_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `registered` datetime NOT NULL,
  PRIMARY KEY (`player_id`),
  KEY `IDX_FK_discord_player` (`player_id`),
  CONSTRAINT `FK_discord_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `ignored`
--
CREATE TABLE `ignored` (
  `ignorer_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `ignoree_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `ignored_at` datetime NOT NULL,
  `state` tinyint(1) NOT NULL,
  PRIMARY KEY (ignorer_id, ignoree_id),
  CONSTRAINT `FK_ignore_ignorer` FOREIGN KEY (`ignorer_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK_ignore_ingnoree` FOREIGN KEY (`ignoree_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `bug`
--
CREATE TABLE `bug` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `reporter_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `handled_by_id` binary(16) DEFAULT NULL COMMENT '(DC2Type:uuid)',
  `message` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resolved_message` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reported_at` datetime NOT NULL,
  `acknowledged_at` datetime DEFAULT NULL,
  `resolved_at` datetime DEFAULT NULL,
  `position_server` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `discord_message_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `IDX_FK_bug_reporter` (`reporter_id`),
   KEY `IDX_FK_bug_handler_by` (`handled_by_id`),
   CONSTRAINT `FK_bug_reporter` FOREIGN KEY (`reporter_id`) REFERENCES `player` (`id`),
   CONSTRAINT `FK_bug_handler_by` FOREIGN key (`handled_by_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `report`
--
CREATE TABLE `report` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `reporter_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `reportee_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `handled_by_id` binary(16) DEFAULT NULL COMMENT '(DC2Type:uuid)',
  `message` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reported_at` datetime NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `acknowledged_at` datetime DEFAULT NULL,
  `resolved_at` datetime DEFAULT NULL,
  `position_server` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `discord_message_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_FK_report_reporter` (`reporter_id`),
  KEY `IDX_FK_report_reportee` (`reportee_id`),
  KEY `IDX_FK_report_handled_by` (`handled_by_id`),
  CONSTRAINT `FK_report_reporter` FOREIGN KEY (`reporter_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK_report_reportee` FOREIGN KEY (`reportee_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK_report_handled_by` FOREIGN KEY (`handled_by_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `help_ops`
--
CREATE TABLE `help_ops` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `player_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `handled_by_id` binary(16) DEFAULT NULL COMMENT '(DC2Type:uuid)',
  `message` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `requested_at` datetime NOT NULL,
  `acknowledged_at` datetime DEFAULT NULL,
  `resolved_at` datetime DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `position_x` int(11) DEFAULT NULL,
  `position_y` int(11) DEFAULT NULL,
  `position_z` int(11) DEFAULT NULL,
  `position_world` varchar(255) DEFAULT NULL,
  `position_server` varchar(255) NOT NULL,
  `discord_message_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_FK_helpop_player` (`player_id`),
  KEY `IDX_FK_helpop_handled_by` (`handled_by_id`),
  CONSTRAINT `FK_helpop_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK_helpop_handled_by` FOREIGN KEY (`handled_by_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `role`
--
CREATE TABLE `role` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `user`
--
CREATE TABLE `user` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `preferred_language` varchar(6) COLLATE utf8mb4_unicode_ci NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `roles` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `blocked` tinyint(1) NOT NULL,
  `role_id` binary(16) DEFAULT NULL COMMENT '(DC2Type:uuid)',
  `player_id` binary(16) COMMENT '(DC2Type:uuid)',
  PRIMARY KEY (`id`),
  KEY `IDX_8D93D649D60322AC` (`role_id`),
  CONSTRAINT `FK_8D93D649D60322AC` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_user_player` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `player_note`
--
CREATE TABLE `player_note` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` binary(16) DEFAULT NULL COMMENT '(DC2Type:uuid)',
  `player_id` binary(16) DEFAULT NULL COMMENT '(DC2Type:uuid)',
  `message` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX_6DA7AA27A76ED395` (`user_id`),
  KEY `IDX_6DA7AA2799E6F5DF` (`player_id`),
  CONSTRAINT `FK_6DA7AA2799E6F5DF` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`),
  CONSTRAINT `FK_6DA7AA27A76ED395` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `permission`
--
CREATE TABLE `permission` (
  `id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Table structure for table `doctrine_migration_versions`
--
CREATE TABLE `doctrine_migration_versions` (
  `version` varchar(191) COLLATE utf8_unicode_ci NOT NULL,
  `executed_at` datetime DEFAULT NULL,
  `execution_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Table structure for table `permission_role`
--
CREATE TABLE `permission_role` (
  `permission_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  `role_id` binary(16) NOT NULL COMMENT '(DC2Type:uuid)',
  PRIMARY KEY (`permission_id`,`role_id`),
  KEY `IDX_6A711CAFED90CCA` (`permission_id`),
  KEY `IDX_6A711CAD60322AC` (`role_id`),
  CONSTRAINT `FK_6A711CAD60322AC` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_6A711CAFED90CCA` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



DROP PROCEDURE IF EXISTS `sp_StartSession`;
DROP PROCEDURE IF EXISTS `sp_StopSession`;



DELIMITER $$

--
-- Procedure `sp_StartSession`
--
CREATE PROCEDURE sp_StartSession (
    IN session_id BINARY(16),
    IN timestamp DATETIME,
    IN player_id BINARY(16),
    IN player_name VARCHAR(255),
	IN player_ip_address VARCHAR(255),
    IN player_version INT
)
BEGIN
    INSERT INTO player (id, name, created_at, online, social_spy, vanished) VALUES (player_id, player_name, timestamp, 1, 0, 0) ON DUPLICATE KEY UPDATE name = player_name, online = 1;
    INSERT INTO session (id, player_id, started_at, ip_address, version, player_name) VALUES (session_id, player_id, timestamp, player_ip_address, player_version, player_name);

    SELECT display_name,
           locale,
           ( SELECT playtime FROM v_Playtime PT WHERE PT.player_id = player_id ) AS playtime,
           social_spy,
           vanished,
           DU.discord_id
    FROM player
    LEFT JOIN discord_user DU ON id = DU.player_id
    WHERE id = player_id;
END$$

--
-- Procedure `sp_StopSession`
--
CREATE PROCEDURE sp_StopSession (
    IN session_id BINARY(16),
    IN player_id BINARY(16),
    IN timestamp DATETIME,
    IN player_online TINYINT(1)
)
BEGIN
    UPDATE player SET online = player_online WHERE id = player_id;
    UPDATE session SET ended_at = timestamp WHERE id = session_id;
END$$

DELIMITER ;



--
-- View `v_Helpops`
--
CREATE OR REPLACE VIEW v_Helpops
AS
SELECT H.id,
       H.player_id,
       P1.name AS player_name,
       H.handled_by_id,
       P2.name AS handled_by_name,
       H.message,
       H.requested_at,
       H.acknowledged_at,
       H.resolved_at,
       H.status,
       H.position_x,
       H.position_y,
       H.position_z,
       H.position_world,
       H.position_server,
       H.discord_message_id
FROM help_ops H
LEFT JOIN player P1 ON H.player_id = P1.id
LEFT JOIN player P2 ON H.handled_by_id = P2.id
WHERE resolved_at IS NULL;

--
-- View `v_Reports`
--
CREATE OR REPLACE VIEW v_Reports
AS
SELECT R.id,
       R.reporter_id,
       P1.name AS reporter_name,
       R.reportee_id,
       P2.name AS reportee_name,
       R.handled_by_id,
       P3.name AS handled_by_name,
       R.message,
       R.reported_at,
       R.status,
       R.acknowledged_at,
       R.resolved_at,
       R.position_server,
       R.discord_message_id
FROM report R
LEFT JOIN player P1 ON R.reporter_id = P1.id
LEFT JOIN player P2 ON R.reportee_id = P2.id
LEFT JOIN player P3 ON R.handled_by_id = P3.id
WHERE resolved_at IS NULL;

--
-- View `v_Bugs`
--
CREATE OR REPLACE VIEW v_Bugs
AS
SELECT B.id,
       B.reporter_id,
       P1.name AS reporter_name,
       B.handled_by_id,
       P2.name AS handled_by_name,
       B.message,
       B.resolved_message,
       B.reported_at,
       B.acknowledged_at,
       B.resolved_at,
       B.position_server,
       B.discord_message_id
FROM bug B
LEFT JOIN player P1 ON B.reporter_id = P1.id
LEFT JOIN player P2 ON B.handled_by_id = P2.id
WHERE resolved_at IS NULL;


--
-- View `v_Playtime`
--
CREATE OR REPLACE VIEW v_Playtime
AS
SELECT S.player_id AS player_id, SUM(TIMESTAMPDIFF(SECOND, S.started_at, IFNULL(S.ended_at, S.started_at))) AS playtime
FROM session S
GROUP BY S.player_id;

--
-- View `v_Lookup`
--
CREATE OR REPLACE VIEW v_Lookup
AS
SELECT P.id,
       DU.discord_id,
       P.name,
       P.display_name,
       P.online,
       P.locale,
       LS.version,
       LS.ip_address AS ip,
       ( SELECT GROUP_CONCAT(DISTINCT player_name ORDER BY player_name ASC SEPARATOR ', ') AS alts FROM session S LEFT JOIN player ON S.player_id = P.id WHERE S.ip_address IN ( SELECT DISTINCT ip_address FROM session WHERE player_id = P.id ) ) AS alts,
       FS.started_at AS first_login,
       LS.started_at AS last_login,
       LS.ended_at AS last_logout,
       ( SELECT playtime FROM v_Playtime WHERE player_id = P.id ) AS playtime
FROM player P
LEFT JOIN discord_user DU ON P.id = DU.player_id
LEFT JOIN ( SELECT s1.* FROM session s1 LEFT JOIN session s2 ON (s1.player_id = s2.player_id AND s1.started_at < s2.started_at) WHERE s2.player_id IS NULL ) LS ON LS.player_id = P.id
LEFT JOIN ( SELECT s1.* FROM session s1 LEFT JOIN session s2 ON (s1.player_id = s2.player_id AND s1.started_at > s2.started_at) WHERE s2.player_id IS NULL ) FS ON FS.player_id = P.id;




CREATE OR REPLACE VIEW v_PlayerInfo
AS
SELECT P.id,
       P.name,
       P.display_name,
       P.online,
       P.locale
FROM player P
