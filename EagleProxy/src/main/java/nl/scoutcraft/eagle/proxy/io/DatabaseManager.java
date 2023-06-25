package nl.scoutcraft.eagle.proxy.io;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.*;
import nl.scoutcraft.eagle.libs.server.ServerInfo;
import nl.scoutcraft.eagle.libs.sql.SQLCache;
import nl.scoutcraft.eagle.libs.sql.SQLManager;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager extends SQLManager {

    // Player & Session Queries
    private static final String SQL_START_SESSION = "CALL sp_StartSession(?, ?, ?, ?, ?, ?);";
    private static final String SQL_STOP_SESSION = "CALL sp_StopSession(?, ?, ?, ?);";
    private static final String SQL_LOOKUP = "SELECT * FROM v_Lookup WHERE name = ? OR display_name = ?;";
    private static final String SQL_LAST_SESSION_ID = "SELECT started_at, ended_at FROM session WHERE player_id = ? ORDER BY started_at DESC LIMIT 1;";
    private static final String SQL_SET_SOCIALSPY = "UPDATE player SET social_spy = ? WHERE id = ?;";
    private static final String SQL_SET_DISPLAYNAME = "UPDATE player SET display_name = ? WHERE id = ?;";
    private static final String SQL_SET_LOCALE = "UPDATE player SET locale = ? WHERE id = ?;";
    private static final String SQL_GET_PLAYER_INFO = "SELECT P.id, P.name, P.display_name, P.online, P.locale FROM player P WHERE P.name = ? OR P.display_name = ?;";
    private static final String SQL_GET_PLAYTIME_TOP = "SELECT name, playtime FROM v_Playtime LEFT JOIN player ON id = player_id ORDER BY playtime DESC LIMIT 10;";
    private static final String SQL_GET_PLAYTIME = "SELECT playtime FROM v_Playtime WHERE player_id = ?;";

    // Chat Queries
    private static final String SQL_INSERT_CHAT = "INSERT INTO chat (id, player_id, server_id, timestamp, message, player_name) VALUES (?, ?, ?, ?, ?, ( SELECT name FROM player WHERE id = ?));";

    // Ignore Queries
    private static final String SQL_GET_IGNORES = "SELECT ignoree_id AS id, name, display_name FROM ignored LEFT JOIN player ON ignoree_id = id WHERE state = 1 AND ignorer_id = ?;";
    private static final String SQL_IGNORE = "INSERT INTO ignored (ignorer_id, ignoree_id, ignored_at, state) VALUES (?, ?, ?, 1) ON DUPLICATE KEY UPDATE ignored_at = ?, state = 1;";
    private static final String SQL_UNIGNORE = "UPDATE ignored SET state = 0 WHERE ignorer_id = ? AND ignoree_id = ?;";
    private static final String SQL_GET_IGNORED_BY = "SELECT ignorer_id FROM ignored WHERE state = 1 AND ignoree_id = ?;";

    // Helpop Queries
    private static final String SQL_INSERT_HELPOP = "INSERT INTO help_ops (id, player_id, message, requested_at, position_server) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE_HELPOP_LOCATION = "UPDATE help_ops SET position_X = ?, position_y = ?, position_z = ?, position_world = ? WHERE id = ?;";
    private static final String SQL_UPDATE_HELPOP_MESSAGE_ID = "UPDATE help_ops SET discord_message_id = ? WHERE id = ?;";
    private static final String SQL_GET_HELPOP = "SELECT * FROM v_Helpops WHERE id = ?;";
    private static final String SQL_GET_UNASSIGNED_HELPOPS = "SELECT * FROM v_Helpops WHERE handled_by_id IS NULL ORDER BY requested_at ASC;";
    private static final String SQL_GET_UNASSIGNED_HELPOP_COUNT = "SELECT count(*) AS count FROM help_ops WHERE resolved_at IS NULL AND handled_by_id IS NULL;";
    private static final String SQL_GET_ASSIGNED_HELPOPS = "SELECT * FROM v_Helpops WHERE handled_by_id = ? AND resolved_at IS NULL ORDER BY requested_at ASC;";
    private static final String SQL_GET_ASSIGNED_HELPOP_COUNT = "SELECT count(*) AS count FROM help_ops WHERE resolved_at IS NULL AND handled_by_id = ?;";
    private static final String SQL_ACCEPT_HELPOP = "UPDATE help_ops SET handled_by_id = ?, acknowledged_at = ? WHERE id = ?;";
    private static final String SQL_CLOSE_HELPOP = "UPDATE help_ops SET resolved_at = ? WHERE id = ?;";

    // Report Queries
    private static final String SQL_INSERT_REPORT = "INSERT INTO report (id, reporter_id, reportee_id, message, reported_at, position_server) VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE_REPORT_MESSAGE_ID = "UPDATE report SET discord_message_id = ? WHERE id = ?;";
    private static final String SQL_GET_REPORT = "SELECT * FROM v_Reports WHERE id = ?;";
    private static final String SQL_GET_UNASSIGNED_REPORTS = "SELECT * FROM v_Reports WHERE handled_by_id IS NULL ORDER BY reported_at ASC;";
    private static final String SQL_GET_UNASSIGNED_REPORT_COUNT = "SELECT count(*) AS count FROM report WHERE resolved_at IS NULL AND handled_by_id IS NULL;";
    private static final String SQL_GET_ASSIGNED_REPORTS = "SELECT * FROM v_Reports WHERE handled_by_id = ? AND resolved_at IS NULL ORDER BY reported_at ASC;";
    private static final String SQL_GET_ASSIGNED_REPORT_COUNT = "SELECT count(*) AS count FROM report WHERE resolved_at IS NULL AND handled_by_id = ?;";
    private static final String SQL_ACCEPT_REPORT = "UPDATE report SET handled_by_id = ?, acknowledged_at = ? WHERE id = ?;";
    private static final String SQL_CLOSE_REPORT = "UPDATE report SET resolved_at = ? WHERE id = ?;";

    // Bug Queries
    private static final String SQL_INSERT_BUG = "INSERT INTO bug (id, reporter_id, message, reported_at, position_server) VALUES (?, ?, ?, ?, ?);";
    private static final String SQL_UPDATE_BUG_MESSAGE_ID = "UPDATE bug SET discord_message_id = ? WHERE id = ?;";
    private static final String SQL_GET_BUG = "SELECT * FROM v_Bugs WHERE id = ?;";
    private static final String SQL_GET_UNASSIGNED_BUGS = "SELECT * FROM v_Bugs WHERE handled_by_id IS NULL ORDER BY reported_at ASC;";
    private static final String SQL_GET_UNASSIGNED_BUG_COUNT = "SELECT count(*) AS count FROM bug WHERE resolved_at IS NULL AND handled_by_id IS NULL;";
    private static final String SQL_GET_ASSIGNED_BUGS = "SELECT * FROM v_Bugs WHERE handled_by_id = ? AND resolved_at IS NULL ORDER BY reported_at ASC;";
    private static final String SQL_GET_ASSIGNED_BUG_COUNT = "SELECT count(*) AS count FROM bug WHERE resolved_at IS NULL AND handled_by_id = ?;";
    private static final String SQL_ACCEPT_BUG = "UPDATE bug SET handled_by_id = ?, acknowledged_at = ? WHERE id = ?;";
    private static final String SQL_CLOSE_BUG = "UPDATE bug SET resolved_at = ?, resolved_message = ? WHERE id = ?;";

    // Server Queries
    private static final String SQL_GET_SERVERS = "SELECT * FROM server WHERE NOT state = 'disabled';";
    private static final String SQL_GET_SERVER_HEARTBEATS = "SELECT name, last_heartbeat FROM server;";
    private static final String SQL_INSERT_SERVER = "INSERT INTO server (id, name, display_name, ip_address, port, state) VALUES (?, ?, ?, ?, ?, 'offline');";
    private static final String SQL_DELETE_SERVER = "UPDATE server SET state = 'disabled' WHERE name = ?;";
    private static final String SQL_UPDATE_SERVER = "UPDATE server SET name = ?, display_name = ?, ip_address = ?, port = ? WHERE id = ?;";
    private static final String SQL_UPDATE_SERVER_STATE = "UPDATE server SET state = ? WHERE id = ?;";
    private static final String SQL_UPDATE_SERVER_TPS = "UPDATE server SET tps = ? WHERE id = ?;";

    // Server Group Queries
    private static final String SQL_GET_SERVERGROUPS = "SELECT name FROM servergroup;";
    private static final String SQL_GET_SERVERS_IN_GROUPS = "SELECT server_id, name FROM server_in_group LEFT JOIN servergroup ON id = group_id;";
    private static final String SQL_INSERT_GROUP = "INSERT INTO servergroup (name) VALUES (?);";
    private static final String SQL_DELETE_GROUP = "DELETE FROM servergroup WHERE name = ?;";
    private static final String SQL_ADD_SERVER_TO_GROUP = "INSERT INTO server_in_group (server_id, group_id) VALUES (?, ( SELECT id FROM servergroup WHERE name = ? ));";
    private static final String SQL_REMOVE_SERVER_FROM_GROUP = "DELETE FROM server_in_group WHERE server_id = ? AND group_id = ( SELECT id FROM servergroup WHERE name = ? );";
    private static final String SQL_REMOVE_ALL_SERVERS_FROM_GROUP = "DELETE FROM server_in_group WHERE group_id = ( SELECT id FROM servergroup WHERE name = ? );";

    // Discord Queries
    private static final String SQL_REGISTER_DISCORD = "INSERT INTO discord_user (player_id, discord_id, registered) VALUES (?, ?, ?);";
    private static final String SQL_GET_DISCORD_PLAYER = "SELECT id, name, display_name FROM discord_user LEFT JOIN player ON player_id = id WHERE discord_id = ?;";
    private static final String SQL_GET_DISCORD_ID = "SELECT discord_id FROM player LEFT JOIN discord_user ON id = player_id WHERE name = ?;";
    private static final String SQL_UNREGISTER_DISCORD = "DELETE FROM discord_user WHERE player_id = ?;";
    private static final String SQL_UNREGISTER_DISCORD_ID = "DELETE FROM discord_user WHERE discord_id = ?;";

    private static final Logger LOGGER = Logger.getLogger("Eagle DB");

    private final EagleProxy plugin;
    private final SQLCache<PlayerInfo> cache;

    public DatabaseManager(EagleProxy plugin, ConfigAdaptor config) {
        super(  config.getNode("mysql", "host").getString(),
                config.getNode("mysql", "port").getInt(),
                config.getNode("mysql", "database").getString(),
                config.getNode("mysql", "username").getString(),
                config.getNode("mysql", "password").getString());

        this.plugin = plugin;
        this.cache = new PlayerInfoCache();

        super.getDatabase(config.getNode("mysql", "database").getString());
    }

    public void startSession(ScoutPlayer sp, Player player, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_START_SESSION)) {

            ps.setBytes(1, this.toBinary(sp.getSessionId()));
            ps.setObject(2, timestamp);
            ps.setBytes(3, this.toBinary(player.getUniqueId()));
            ps.setString(4, player.getUsername());
            ps.setString(5, player.getRemoteAddress().getAddress().toString().replaceFirst("/", ""));
            ps.setInt(6, player.getProtocolVersion().getProtocol());
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                throw new SQLException("Did not receive data from eagle DB sp_StartSession!");

            sp.setDisplayName(rs.getString("display_name"));
            sp.setSpying(rs.getBoolean("social_spy"));
            sp.setVanished(rs.getBoolean("vanished"));
            sp.setRawPlaytime(rs.getInt("playtime"));
            sp.setLocale(Optional.ofNullable(rs.getString("locale")).map(Locale::forLanguageTag).orElse(null));
            sp.setDiscordId(rs.getString("discord_id"));

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to start player session!", exc);
        }
    }

    public void stopSession(UUID sessionId, UUID playerId, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_STOP_SESSION)) {
            ps.setBytes(1, this.toBinary(sessionId));
            ps.setBytes(2, this.toBinary(playerId));
            ps.setObject(3, timestamp);
            ps.setBoolean(4, false);
            ps.execute();

        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to stop player session!", exc);
        }
    }

    public void updatePlaytime(UUID sessionId, UUID playerId, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_STOP_SESSION)) {
            ps.setBytes(1, this.toBinary(sessionId));
            ps.setBytes(2, this.toBinary(playerId));
            ps.setObject(3, timestamp);
            ps.setBoolean(4, true);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to update player playtime!", exc);
        }
    }

    @Nullable
    public PlayerLookupInfo lookup(PlayerInfo info) {
        PlayerLookupInfo result = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LOOKUP)) {
            ps.setString(1, info.getName());
            ps.setString(2, info.getName());
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return null;

            result = new PlayerLookupInfo(
                    this.fromBinary(rs.getBytes("id")),
                    rs.getString("discord_id"),
                    rs.getString("name"),
                    rs.getString("display_name"),
                    rs.getBoolean("online"),
                    rs.getString("locale"),
                    rs.getInt("version"),
                    rs.getString("ip"),
                    rs.getString("alts"),
                    rs.getObject("first_login", LocalDateTime.class),
                    rs.getObject("last_login", LocalDateTime.class),
                    rs.getObject("last_logout", LocalDateTime.class),
                    rs.getInt("playtime")
            );

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to lookup player info!", exc);
        }

        return result;
    }

    @Nullable
    public LocalDateTime getLastLogin(UUID uuid) {
        LocalDateTime timestamp = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LAST_SESSION_ID)) {
            ps.setBytes(1, this.toBinary(uuid));
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                timestamp = rs.getObject("started_at", LocalDateTime.class);

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get player's last login!", exc);
        }

        return timestamp;
    }

    @Nullable
    public LocalDateTime getLastLogout(UUID uuid) {
        LocalDateTime timestamp = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LAST_SESSION_ID)) {
            ps.setBytes(1, this.toBinary(uuid));
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                timestamp = rs.getObject("ended_at", LocalDateTime.class);

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get player's last logout!", exc);
        }

        return timestamp;
    }

    public void setSpying(UUID uuid, boolean value) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SET_SOCIALSPY)) {
            ps.setBoolean(1, value);
            ps.setBytes(2, this.toBinary(uuid));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to set player socialspy!", exc);
        }
    }

    public void setDisplayName(UUID uuid, @Nullable String displayName) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SET_DISPLAYNAME)) {
            ps.setString(1, displayName);
            ps.setBytes(2, this.toBinary(uuid));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to set player displayname!", exc);
        }
    }

    public void setLocale(UUID uuid, @Nullable Locale locale) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SET_LOCALE)) {
            ps.setString(1, locale == null ? null : locale.toLanguageTag());
            ps.setBytes(2, this.toBinary(uuid));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to set player locale!", exc);
        }
    }

    @Nullable
    public PlayerInfo getPlayerInfo(String name) {
        Optional<PlayerInfo> cached = this.cache.stream().filter(pi -> pi.name().equalsIgnoreCase(name) || pi.displayName().equalsIgnoreCase(name)).findFirst();
        if (cached.isPresent())
            return cached.get();

        PlayerInfo info = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_PLAYER_INFO)) {
            ps.setString(1, name);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                info = new PlayerInfo(this.fromBinary(rs.getBytes("id")), rs.getString("name"), rs.getString("display_name"));

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get player info!", exc);
        }

        return info;
    }

    public Map<String, Integer> getPlaytimeTop() {
        Map<String, Integer> top = new LinkedHashMap<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_PLAYTIME_TOP);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                top.put(rs.getString("name"), rs.getInt("playtime"));

        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to update player playtime!", exc);
        }

        return top;
    }

    public Optional<Integer> getPlaytime(UUID uuid) {
        Integer playtime = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_PLAYTIME)) {
            ps.setBytes(1, this.toBinary(uuid));
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                playtime = rs.getInt("playtime");

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get player playtime!", exc);
        }

        return Optional.ofNullable(playtime);
    }

    // Chat Queries

    public void addChatMessage(Player player, String message) {
        byte[] chatId = this.toBinary(UUID.randomUUID());
        byte[] playerId = this.toBinary(player.getUniqueId());
        byte[] serverId = this.toBinary(this.plugin.getServerManager().getServer(player.getCurrentServer().get().getServerInfo()).getId());

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_CHAT)) {
            ps.setBytes(1, chatId);
            ps.setBytes(2, playerId);
            ps.setBytes(3, serverId);
            ps.setObject(4, LocalDateTime.now());
            ps.setString(5, message);
            ps.setBytes(6, playerId);
            ps.execute();

        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to log chat message!", exc);
        }
    }

    // Ignore Queries

    public List<PlayerInfo> getIgnores(UUID uuid) {
        List<PlayerInfo> ignores = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_IGNORES)) {
            ps.setBytes(1, this.toBinary(uuid));
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                ignores.add(new PlayerInfo(this.fromBinary(rs.getBytes("id")), rs.getString("name"), rs.getString("display_name")));

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get player's ignores!", exc);
        }

        return ignores;
    }

    public void ignore(UUID ignorer, UUID ignoree, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_IGNORE)) {
            ps.setBytes(1, this.toBinary(ignorer));
            ps.setBytes(2, this.toBinary(ignoree));
            ps.setObject(3, timestamp);
            ps.setObject(4, timestamp);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to ignore player!", exc);
        }
    }

    public void unignore(UUID ignorer, UUID ignoree) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UNIGNORE)) {
            ps.setBytes(1, this.toBinary(ignorer));
            ps.setBytes(2, this.toBinary(ignoree));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to unignore player!", exc);
        }
    }

    public List<String> getIgnoredByList(UUID playerId) {
        List<String> list = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_IGNORED_BY)) {
            ps.setBytes(1, this.toBinary(playerId));
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                list.add(this.fromBinary(rs.getBytes("ignorer_id")).toString());

        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to 'ignored by' list!", exc);
        }

        return list;
    }

    // Helpop Queries

    public void addHelpop(UUID id, UUID playerId, String message, LocalDateTime timestamp, String serverName) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_HELPOP)) {
            ps.setBytes(1, this.toBinary(id));
            ps.setBytes(2, this.toBinary(playerId));
            ps.setString(3, message);
            ps.setObject(4, timestamp);
            ps.setString(5, serverName);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to insert helpop!", exc);
        }
    }

    public void addLocationToHelpop(UUID id, PlayerLocationInfo info) {
        if (info.getWorld() == null) return;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_HELPOP_LOCATION)) {
            ps.setInt(1, info.getX());
            ps.setInt(2, info.getY());
            ps.setInt(3, info.getZ());
            ps.setString(4, info.getWorld());
            ps.setBytes(5, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to add location info to helpop!", exc);
        }
    }

    public void addDiscordMessageIdToHelpop(UUID id, String messageId) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_HELPOP_MESSAGE_ID)) {
            ps.setString(1, messageId);
            ps.setBytes(2, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to add discord message id to helpop!", exc);
        }
    }

    @Nullable
    public HelpopInfo getHelpop(UUID id) {
        HelpopInfo helpop = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_HELPOP)) {
            ps.setBytes(1, this.toBinary(id));
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                helpop = this.getHelpop(rs);

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get helpop from id!", exc);
        }

        return helpop;
    }

    public List<HelpopInfo> getUnassignedHelpops() {
        List<HelpopInfo> helpops = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_UNASSIGNED_HELPOPS);
             ResultSet rs = ps.executeQuery()) {
            this.addHelpops(rs, helpops);
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned helpops!", exc);
        }

        return helpops;
    }

    public int getUnassignedHelpopCount() {
        int count = 0;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_UNASSIGNED_HELPOP_COUNT);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                count = rs.getInt("count");
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned helpop count!", exc);
        }

        return count;
    }

    public List<HelpopInfo> getAssignedHelpops(UUID uuid) {
        List<HelpopInfo> helpops = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ASSIGNED_HELPOPS)) {
            ps.setBytes(1, this.toBinary(uuid));
            ResultSet rs = ps.executeQuery();
            this.addHelpops(rs, helpops);
            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get assigned helpops for '" + uuid + "'!", exc);
        }

        return helpops;
    }

    public int getAssignedHelpopCount(UUID playerId) {
        int count = 0;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ASSIGNED_HELPOP_COUNT)) {
            ps.setBytes(1, this.toBinary(playerId));
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                count = rs.getInt("count");
            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned helpop count!", exc);
        }

        return count;
    }

    public void acceptHelpop(UUID id, UUID playerId, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ACCEPT_HELPOP)) {
            ps.setBytes(1, this.toBinary(playerId));
            ps.setObject(2, timestamp);
            ps.setBytes(3, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to accept helpop '" + id + "'!", exc);
        }
    }

    public void closeHelpop(UUID id, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CLOSE_HELPOP)) {
            ps.setObject(1, timestamp);
            ps.setBytes(2, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to close helpop '" + id + "'!", exc);
        }
    }

    private void addHelpops(ResultSet rs, List<HelpopInfo> helpops) throws SQLException {
        while (rs.next())
            helpops.add(this.getHelpop(rs));
    }

    private HelpopInfo getHelpop(ResultSet rs) throws SQLException {
        return new HelpopInfo(
                this.fromBinary(rs.getBytes("id")),
                this.fromBinary(rs.getBytes("player_id")),
                rs.getString("player_name"),
                this.fromBinary(rs.getBytes("handled_by_id")),
                rs.getString("handled_by_name"),
                rs.getString("message"),
                rs.getObject("requested_at", LocalDateTime.class),
                rs.getObject("acknowledged_at", LocalDateTime.class),
                rs.getObject("resolved_at", LocalDateTime.class),
                rs.getString("status"),
                rs.getInt("position_x"),
                rs.getInt("position_y"),
                rs.getInt("position_z"),
                rs.getString("position_world"),
                rs.getString("position_server"),
                rs.getString("discord_message_id"));
    }

    // Report Queries

    public void addReport(UUID id, UUID reporterId, UUID reporteeId, String message, LocalDateTime timestamp, String serverName) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_REPORT)) {
            ps.setBytes(1, this.toBinary(id));
            ps.setBytes(2, this.toBinary(reporterId));
            ps.setBytes(3, this.toBinary(reporteeId));
            ps.setString(4, message);
            ps.setObject(5, timestamp);
            ps.setString(6, serverName);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to insert report!", exc);
        }
    }

    public void addDiscordMessageIdToReport(UUID id, String messageId) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_REPORT_MESSAGE_ID)) {
            ps.setString(1, messageId);
            ps.setBytes(2, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to add discord message id to report!", exc);
        }
    }

    @Nullable
    public ReportInfo getReport(UUID id) {
        ReportInfo report = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_REPORT)) {
            ps.setBytes(1, this.toBinary(id));
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                report = this.getReport(rs);

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get report from id!", exc);
        }

        return report;
    }

    public List<ReportInfo> getUnassignedReports() {
        List<ReportInfo> reports = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_UNASSIGNED_REPORTS);
             ResultSet rs = ps.executeQuery()) {
            this.addReports(rs, reports);
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned reports!", exc);
        }

        return reports;
    }

    public int getUnassignedReportCount() {
        int count = 0;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_UNASSIGNED_REPORT_COUNT);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                count = rs.getInt("count");
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned report count!", exc);
        }

        return count;
    }

    public List<ReportInfo> getAssignedReports(UUID uuid) {
        List<ReportInfo> reports = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ASSIGNED_REPORTS)) {
            ps.setBytes(1, this.toBinary(uuid));
            ResultSet rs = ps.executeQuery();
            this.addReports(rs, reports);
            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get assigned reports for '" + uuid + "'!", exc);
        }

        return reports;
    }

    public int getAssignedReportCount(UUID playerId) {
        int count = 0;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ASSIGNED_REPORT_COUNT)) {
            ps.setBytes(1, this.toBinary(playerId));
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                count = rs.getInt("count");
            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned report count!", exc);
        }

        return count;
    }

    public void acceptReport(UUID id, UUID playerId, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ACCEPT_REPORT)) {
            ps.setBytes(1, this.toBinary(playerId));
            ps.setObject(2, timestamp);
            ps.setBytes(3, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to accept report '" + id + "'!", exc);
        }
    }

    public void closeReport(UUID id, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CLOSE_REPORT)) {
            ps.setObject(1, timestamp);
            ps.setBytes(2, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to close report '" + id + "'!", exc);
        }
    }

    private void addReports(ResultSet rs, List<ReportInfo> reports) throws SQLException {
        while (rs.next())
            reports.add(this.getReport(rs));
    }

    private ReportInfo getReport(ResultSet rs) throws SQLException {
        return new ReportInfo(
                this.fromBinary(rs.getBytes("id")),
                this.fromBinary(rs.getBytes("reporter_id")),
                rs.getString("reporter_name"),
                this.fromBinary(rs.getBytes("reportee_id")),
                rs.getString("reportee_name"),
                this.fromBinary(rs.getBytes("handled_by_id")),
                rs.getString("handled_by_name"),
                rs.getString("message"),
                rs.getObject("reported_at", LocalDateTime.class),
                rs.getString("status"),
                rs.getObject("acknowledged_at", LocalDateTime.class),
                rs.getObject("resolved_at", LocalDateTime.class),
                rs.getString("position_server"),
                rs.getString("discord_message_id"));
    }

    // Bug Queries

    public void addBug(UUID id, UUID reporterId, String message, LocalDateTime timestamp, String serverName) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_BUG)) {
            ps.setBytes(1, this.toBinary(id));
            ps.setBytes(2, this.toBinary(reporterId));
            ps.setString(3, message);
            ps.setObject(4, timestamp);
            ps.setString(5, serverName);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to insert bug!", exc);
        }
    }

    public void addDiscordMessageIdToBug(UUID id, String messageId) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_BUG_MESSAGE_ID)) {
            ps.setString(1, messageId);
            ps.setBytes(2, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to add discord message id to bug!", exc);
        }
    }

    @Nullable
    public BugInfo getBug(UUID id) {
        BugInfo bug = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_BUG)) {
            ps.setBytes(1, this.toBinary(id));
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                bug = this.getBug(rs);

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get bug from id!", exc);
        }

        return bug;
    }

    public List<BugInfo> getUnassignedBugs() {
        List<BugInfo> bugs = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_UNASSIGNED_BUGS);
             ResultSet rs = ps.executeQuery()) {
            this.addBugs(rs, bugs);
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned bugs!", exc);
        }

        return bugs;
    }

    public int getUnassignedBugCount() {
        int count = 0;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_UNASSIGNED_BUG_COUNT);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                count = rs.getInt("count");
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned bug count!", exc);
        }

        return count;
    }

    public List<BugInfo> getAssignedBugs(UUID uuid) {
        List<BugInfo> bugs = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ASSIGNED_BUGS)) {
            ps.setBytes(1, this.toBinary(uuid));
            ResultSet rs = ps.executeQuery();
            this.addBugs(rs, bugs);
            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get assigned bugs for '" + uuid + "'!", exc);
        }

        return bugs;
    }

    public int getAssignedBugCount(UUID playerId) {
        int count = 0;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_ASSIGNED_BUG_COUNT)) {
            ps.setBytes(1, this.toBinary(playerId));
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                count = rs.getInt("count");
            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get unassigned bug count!", exc);
        }

        return count;
    }

    public void acceptBug(UUID id, UUID playerId, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ACCEPT_BUG)) {
            ps.setBytes(1, this.toBinary(playerId));
            ps.setObject(2, timestamp);
            ps.setBytes(3, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to accept bug '" + id + "'!", exc);
        }
    }

    public void closeBug(UUID id, @Nullable String message, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CLOSE_BUG)) {
            ps.setObject(1, timestamp);
            ps.setString(2, message);
            ps.setBytes(3, this.toBinary(id));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to close bug '" + id + "'!", exc);
        }
    }

    private void addBugs(ResultSet rs, List<BugInfo> bugs) throws SQLException {
        while (rs.next())
            bugs.add(this.getBug(rs));
    }

    private BugInfo getBug(ResultSet rs) throws SQLException {
        return new BugInfo(
                this.fromBinary(rs.getBytes("id")),
                this.fromBinary(rs.getBytes("reporter_id")),
                rs.getString("reporter_name"),
                this.fromBinary(rs.getBytes("handled_by_id")),
                rs.getString("handled_by_name"),
                rs.getString("message"),
                rs.getString("resolved_message"),
                rs.getObject("reported_at", LocalDateTime.class),
                rs.getObject("acknowledged_at", LocalDateTime.class),
                rs.getObject("resolved_at", LocalDateTime.class),
                rs.getString("position_server"),
                rs.getString("discord_message_id"));
    }

    // Server Queries

    public Map<String, ServerInfo> getAllServers() {
        Map<String, ServerInfo> servers = new HashMap<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_SERVERS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                servers.put(name, new ServerInfo(
                        this.fromBinary(rs.getBytes("id")),
                        name,
                        rs.getString("ip_address"),
                        rs.getInt("port"),
                        false,
                        rs.getString("display_name")));
            }
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get all servers!", exc);
        }

        return servers;
    }

    public Map<String, Long> getServerHeartbeats() {
        Map<String, Long> heartbeats = new HashMap<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_SERVER_HEARTBEATS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                heartbeats.put(rs.getString("name"), rs.getLong("last_heartbeat"));

        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get all server groups!", exc);
        }

        return heartbeats;
    }

    public void addServer(UUID id, String name, String ipAddress, int port, @Nullable String displayName) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_SERVER)) {
            ps.setBytes(1, this.toBinary(id));
            ps.setString(2, name);
            ps.setString(3, displayName);
            ps.setString(4, ipAddress);
            ps.setInt(5, port);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to insert server!", exc);
        }
    }

    public void deleteServer(String name) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_SERVER)) {
            ps.setString(1, name);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to delete server!", exc);
        }
    }

    public void updateServer(ScoutServer server) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_SERVER)) {
            ps.setString(1, server.getName());
            ps.setString(2, server.getDisplayName());
            ps.setString(3, server.getIp());
            ps.setInt(4, server.getPort());
            ps.setBytes(5, this.toBinary(server.getId()));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to update server!", exc);
        }
    }

    public void setServerState(UUID serverId, boolean state) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_SERVER_STATE)) {
            ps.setString(1, state ? "online" : "offline");
            ps.setBytes(2, this.toBinary(serverId));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to update server state!", exc);
        }
    }

    public void setServerTPS(UUID serverId, double tps) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_SERVER_TPS)) {
            ps.setDouble(1, tps);
            ps.setBytes(2, this.toBinary(serverId));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to update server tps!", exc);
        }
    }

    // Server Group Queries

    public List<String> getAllServerGroups() {
        List<String> groups = new ArrayList<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_SERVERGROUPS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                groups.add(rs.getString("name"));

        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get all server groups!", exc);
        }

        return groups;
    }

    public Map<UUID, List<String>> getServersInGroups() {
        Map<UUID, List<String>> groups = new HashMap<>();

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_SERVERS_IN_GROUPS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                groups.computeIfAbsent(this.fromBinary(rs.getBytes("server_id")), k -> new ArrayList<>()).add(rs.getString("name"));

        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get all servers in groups!", exc);
        }

        return groups;
    }

    public void addGroup(String group) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_GROUP)) {
            ps.setString(1, group);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to insert servergroup!", exc);
        }
    }

    public void deleteGroup(String group) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE_GROUP)) {
            ps.setString(1, group);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to delete servergroup!", exc);
        }
    }

    public void addServerToGroup(UUID serverId, String group) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ADD_SERVER_TO_GROUP)) {
            ps.setBytes(1, this.toBinary(serverId));
            ps.setString(2, group);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to insert server!", exc);
        }
    }

    public void removeServerFromGroup(UUID serverId, String group) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REMOVE_SERVER_FROM_GROUP)) {
            ps.setBytes(1, this.toBinary(serverId));
            ps.setString(2, group);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to remove server from group!", exc);
        }
    }

    public void removeAllServersFromGroup(String group) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REMOVE_ALL_SERVERS_FROM_GROUP)) {
            ps.setString(1, group);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to remove all servers from group!", exc);
        }
    }

    // Discord Queries

    public void registerDiscord(UUID playerId, String discordId, LocalDateTime timestamp) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REGISTER_DISCORD)) {
            ps.setBytes(1, this.toBinary(playerId));
            ps.setString(2, discordId);
            ps.setObject(3, timestamp);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to register discord user!", exc);
        }
    }

    public Optional<PlayerInfo> getDiscordPlayerInfo(String discordId) {
        PlayerInfo playerId = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_DISCORD_PLAYER)) {
            ps.setString(1, discordId);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                playerId = new PlayerInfo(this.fromBinary(rs.getBytes("id")), rs.getString("name"), rs.getString("display_name"));

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to check if discord user is registered!", exc);
        }

        return Optional.ofNullable(playerId);
    }

    @Nullable
    public String getDiscordId(String playerName) {
        String id = null;

        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_DISCORD_ID)) {
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                id = rs.getString("discord_id");

            rs.close();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to get discord id for '" + playerName +"'!", exc);
        }

        return id;
    }

    public void unregisterDiscord(UUID playerId) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UNREGISTER_DISCORD)) {
            ps.setBytes(1, this.toBinary(playerId));
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to unregister discord account!", exc);
        }
    }

    public void unregisterDiscord(String discordId) {
        try (Connection conn = super.getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UNREGISTER_DISCORD_ID)) {
            ps.setString(1, discordId);
            ps.execute();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to unregister discord account!", exc);
        }
    }

    private UUID fromBinary(byte[] bytes) {
        if (bytes == null || bytes.length < 16) return null;

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    private byte[] toBinary(UUID uuid) {
        return ByteBuffer.wrap(new byte[16])
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }
}
