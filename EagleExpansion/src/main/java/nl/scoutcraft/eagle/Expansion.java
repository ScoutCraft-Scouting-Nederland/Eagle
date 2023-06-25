package nl.scoutcraft.eagle;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.server.ServerManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class Expansion extends PlaceholderExpansion {

    private static final String DEFAULT_STATE = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Offline";
    private static final ServerManager MANAGER = EagleServer.getInstance().getServerManager();

    @Override
    @NotNull
    public String getIdentifier() {
        return "eagle";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "Acixsi";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "2.0";
    }

    public Expansion() {}

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        return Optional.ofNullable(MANAGER.getGameState(identifier)).orElse(DEFAULT_STATE);
    }
}
