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
