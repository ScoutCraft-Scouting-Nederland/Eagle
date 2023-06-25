CREATE VIEW v_Lookup
AS
SELECT P.id,
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
LEFT JOIN ( SELECT s1.* FROM session s1 LEFT JOIN session s2 ON (s1.player_id = s2.player_id AND s1.started_at < s2.started_at) WHERE s2.player_id IS NULL ) LS ON LS.player_id = P.id
LEFT JOIN ( SELECT s1.* FROM session s1 LEFT JOIN session s2 ON (s1.player_id = s2.player_id AND s1.started_at > s2.started_at) WHERE s2.player_id IS NULL ) FS ON FS.player_id = P.id;

