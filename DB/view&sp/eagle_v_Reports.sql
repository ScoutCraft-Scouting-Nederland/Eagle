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
