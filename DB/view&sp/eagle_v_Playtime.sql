CREATE VIEW v_Playtime
AS
SELECT S.player_id AS player_id,
       SUM(TIMESTAMPDIFF(SECOND, S.started_at, IFNULL(S.ended_at, S.started_at))) AS playtime
FROM session S
GROUP BY S.player_id;
