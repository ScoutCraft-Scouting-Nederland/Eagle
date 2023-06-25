package nl.scoutcraft.eagle.proxy.discord;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

public final class DiscordEmbeds {

    public static DiscordEmbed expired() {
        return new DiscordEmbed(DiscordEmbedType.EXPIRED);
    }

    public static DiscordEmbed serverState(String serverName, String serverIp, int port, boolean online) {
        return new DiscordEmbed(DiscordEmbedType.SERVER_STATE, serverName, serverIp, port, online);
    }

    public static DiscordEmbed register(String key) {
        return new DiscordEmbed(DiscordEmbedType.REGISTER, key);
    }

    public static DiscordEmbed success(Player player) {
        return new DiscordEmbed(DiscordEmbedType.SUCCESS, CommandMessages.REGISTER_DISCORD_SUCCESS_TITLE.getString(player), CommandMessages.REGISTER_DISCORD_SUCCESS_DESC.getString(player));
    }

    public static DiscordEmbed helpop(String requester, @Nullable String handler, String server, @Nullable String world, @Nullable Integer x, @Nullable Integer y, @Nullable Integer z, String message, LocalDateTime timestamp, boolean closed) {
        return new DiscordEmbed(DiscordEmbedType.HELPOP, requester, handler, server, world, x, y, z, message, timestamp, closed);
    }

    public static DiscordEmbed report(String reporter, String reportee, @Nullable String handler, String server, String message, LocalDateTime timestamp, boolean closed) {
        return new DiscordEmbed(DiscordEmbedType.REPORT, reporter, reportee, handler, server, message, timestamp, closed);
    }

    public static DiscordEmbed bug(String reporter, @Nullable String handler, String serverName, String message, @Nullable String resolvedMessage, LocalDateTime timestamp, boolean closed) {
        return new DiscordEmbed(DiscordEmbedType.BUG, reporter, handler, serverName, message, resolvedMessage, timestamp, closed);
    }
}
