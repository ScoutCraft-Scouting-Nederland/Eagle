package nl.scoutcraft.eagle.server.listener;

import nl.scoutcraft.eagle.server.EagleServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClientListener implements Listener {

    private final EagleServer plugin;

    public ClientListener(EagleServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.plugin.getPlayerManager().onJoin(event.getPlayer()), 1L);
    }
}
