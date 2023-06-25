package nl.scoutcraft.eagle.proxy.discord;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public interface IDiscordManager {

    void start();
    void close();

    boolean isReady();
    void addReadyAction(Runnable action);

    void onJoin(Player player);
    void onQuit(UUID playerId);

    default void sendMessage(String userId, String message) { this.sendMessage(userId, message, id -> {}); }
    default void sendMessage(String userId, DiscordEmbed embed) { this.sendMessage(userId, embed, id -> {}); }
    void sendMessage(String userId, String message, Consumer<String> idAction);
    void sendMessage(String userId, DiscordEmbed embed, Consumer<String> idAction);
    void editMessage(String userId, String messageId, String message);
    void editMessage(String userId, String embedId, DiscordEmbed embed);

    default void sendChannelMessage(DiscordChannel channel, String message) { this.sendChannelMessage(channel, message, id -> {}); }
    default void sendChannelMessage(DiscordChannel channel, DiscordEmbed embed) { this.sendChannelMessage(channel, embed, id -> {}); }
    void sendChannelMessage(DiscordChannel channel, String message, Consumer<String> idAction);
    void sendChannelMessage(DiscordChannel channel, DiscordEmbed embed, Consumer<String> idAction);
    void editChannelMessage(DiscordChannel channel, String messageId, String message);
    void editChannelMessage(DiscordChannel channel, String embedId, DiscordEmbed embed);

    void updateDiscordNameAndRoles(Player player);

    @Nullable String[] removeToken(String token);

    static IDiscordManager of(ProxyServer proxy, boolean enabled) {
        return proxy.getPluginManager().getPlugin("scotty").isPresent() && enabled ? new DiscordManager() : new EmptyDiscordManager();
    }
}
