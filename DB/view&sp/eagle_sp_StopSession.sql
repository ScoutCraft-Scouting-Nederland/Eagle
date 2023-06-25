
DROP PROCEDURE sp_StopSession;



DELIMITER $$

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



CALL sp_StopSession('1', NOW(), '1')
