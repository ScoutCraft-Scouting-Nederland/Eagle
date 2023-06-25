package nl.scoutcraft.eagle.proxy.server.tasks;

import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.io.ConfigAdaptor;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;
import nl.scoutcraft.eagle.proxy.utils.Task;

import java.util.concurrent.TimeUnit;

public class TPSTask extends Task {

    private final EagleProxy plugin;

    public TPSTask(EagleProxy plugin) {
        super(20, TimeUnit.SECONDS);

        this.plugin = plugin;
        this.load(plugin.getConfigAdaptor());
    }

    public void load(ConfigAdaptor config) {
        super.setInterval(config.getNode("timings.tps_update_millis").getInt(20_000), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        for (ScoutServer server : this.plugin.getServerManager().getServers()) {
            if (!server.getState() || server.getPlayers().isEmpty()) continue;

            this.plugin.getServerManager().getChannel()
                    .<double[]>request()
                    .onResponse(tps -> this.handleTPS(server, tps[0]))
                    .setTarget(server)
                    .getTPS();
        }
    }

    private void handleTPS(ScoutServer server, double tps) {
        this.plugin.getSQLManager().setServerTPS(server.getId(), tps);
    }
}
