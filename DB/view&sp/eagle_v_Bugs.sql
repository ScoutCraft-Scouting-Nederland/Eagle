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
