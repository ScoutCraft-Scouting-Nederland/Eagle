
DELETE FROM eagle_temp.session;
DELETE FROM eagle_temp.server;
DELETE FROM eagle_temp.discord_user;
DELETE FROM eagle_temp.player;

INSERT INTO eagle_temp.player (id, name, created_at, display_name, online, social_spy, vanished)
SELECT UUID_TO_BIN(P.uuid),
       P.username,
       FROM_UNIXTIME(FLOOR(CONVERT(P.firstlogin, SIGNED) / 1000)),
       NULLIF(P.nickname, ''),
       0,
       0,
       0
FROM global_networkmanager.nm_players P;



INSERT INTO eagle_temp.session (id, player_id, started_at, ended_at, ip_address, version, player_name)
SELECT UUID_TO_BIN(UUID()),
       UUID_TO_BIN(S.uuid),
       FROM_UNIXTIME(FLOOR(CONVERT(S.start, SIGNED) / 1000)),
       FROM_UNIXTIME(FLOOR(CONVERT(S.end, SIGNED) / 1000)),
       ( SELECT ip FROM global_networkmanager.nm_players P WHERE P.uuid = S.uuid LIMIT 1 ),
       S.version,
       ( SELECT username FROM global_networkmanager.nm_players P WHERE P.uuid = S.uuid )
FROM global_networkmanager.nm_sessions S;



INSERT INTO eagle_temp.server (id, name, display_name, ip_address, port, state)
SELECT UUID_TO_BIN(UUID()),
       S.servername,
       S.displayname,
       S.ip,
       S.port,
       'offline'
FROM global_networkmanager.nm_servers S;



INSERT INTO eagle_temp.discord_user (player_id, discord_id, registered)
SELECT UUID_TO_BIN(DU.UUID),
       DU.DiscordId,
       FROM_UNIXTIME(FLOOR(CONVERT(DU.registered, SIGNED) / 1000))
FROM global_networkmanager.nm_discordusers DU;
