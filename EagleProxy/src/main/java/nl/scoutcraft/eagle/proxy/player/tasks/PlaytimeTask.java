package nl.scoutcraft.eagle.proxy.player.tasks;

import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.utils.Task;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlaytimeTask extends Task {

    private final EagleProxy plugin;

    public PlaytimeTask(EagleProxy plugin) {
        super(plugin.getConfigAdaptor().getNode("playtime_update_interval_seconds").getInt(180), TimeUnit.SECONDS);

        this.plugin = plugin;
    }

    @Override
    public void run() {
        LocalDateTime timestamp = LocalDateTime.now();

        EagleProxy.getProxy().getAllPlayers().stream()
                .map(this.plugin.getPlayerManager()::getScoutPlayer)
                .filter(Objects::nonNull)
                .forEach(sp -> this.plugin.getSQLManager().updatePlaytime(sp.getSessionId(), sp.getPlayerId(), timestamp));
    }
}
