package nl.scoutcraft.eagle.server.server;

import nl.scoutcraft.eagle.libs.sql.IDatabase;
import nl.scoutcraft.eagle.libs.sql.ISQLManager;
import nl.scoutcraft.eagle.server.EagleServer;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class HeartbeatTask implements Runnable {

    private static final IDatabase SQL = EagleServer.getInstance().getSQLManager().getDefaultDatabase();

    private String serverName;

    public void start(EagleServer plugin) {
        this.serverName = plugin.getConfig().getString("server-name");
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 20L, 20L);
    }

    @Override
    public void run() {
        try (Connection conn = SQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE server SET last_heartbeat = ? WHERE name = ?;")) {

            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, this.serverName);
            ps.execute();

        } catch (SQLException exc) {
            EagleServer.getInstance().getLogger().log(Level.SEVERE, "Failed to send heartbeat to the database!", exc);
        }
    }
}
