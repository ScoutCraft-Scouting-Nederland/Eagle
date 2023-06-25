package nl.scoutcraft.eagle.proxy.server.tasks;

import nl.scoutcraft.eagle.libs.server.GameState;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.io.ConfigAdaptor;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;
import nl.scoutcraft.eagle.proxy.server.ServerGroup;
import nl.scoutcraft.eagle.proxy.utils.Task;

import java.util.concurrent.TimeUnit;

public class HeartbeatCheckTask extends Task {

    private final EagleProxy plugin;

    private int maxHeartbeatMissedMillis;

    public HeartbeatCheckTask(EagleProxy plugin) {
        super(5, TimeUnit.SECONDS);

        this.plugin = plugin;
        this.load(plugin.getConfigAdaptor());
    }

    public void load(ConfigAdaptor config) {
        this.maxHeartbeatMissedMillis = config.getNode("timings", "heartbeat_max_missed_millis").getInt(10_000);
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();

        this.plugin.getSQLManager().getServerHeartbeats().forEach((serverName, heartbeat) -> {
            ScoutServer server = this.plugin.getServerManager().getServer(serverName);
            if (server == null)
                return;

            if (server.getGameState() == null && ServerGroup.GAME.matches(server))
                server.setGameState(GameState.OFFLINE);

            this.plugin.getServerManager().setState(server, heartbeat + this.maxHeartbeatMissedMillis > currentTime);
        });
    }
}
