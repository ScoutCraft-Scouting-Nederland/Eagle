package nl.scoutcraft.eagle.limbo.server;

import com.loohp.limbo.scheduler.LimboTask;
import nl.scoutcraft.eagle.libs.sql.IDatabase;
import nl.scoutcraft.eagle.limbo.EagleLimbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HeartbeatTask implements LimboTask {

    private static final IDatabase SQL = EagleLimbo.getInstance().getSQLManager().getDefaultDatabase();

    private String serverName;

    public void start(EagleLimbo plugin) {
        this.serverName = "Limbo";
        EagleLimbo.getInstance().getServer().getScheduler().runTaskTimerAsync(plugin, this, 20L, 20L);
    }

    @Override
    public void run() {
        try (Connection conn = SQL.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE server SET last_heartbeat = ? WHERE name = ?;")) {

            ps.setLong(1, System.currentTimeMillis());
            ps.setString(2, this.serverName);
            ps.execute();

        } catch (SQLException exc) {
            System.err.println("Failed to send heartbeat to the database!");
            exc.printStackTrace();
        }
    }
}
