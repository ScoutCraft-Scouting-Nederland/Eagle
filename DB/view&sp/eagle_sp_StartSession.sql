
DROP PROCEDURE sp_StartSession;



DELIMITER $$

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

DELIMITER ;



CALL sp_StartSession('1', NOW(), '1', '__Daniel', '217.x.x.x', 255)
