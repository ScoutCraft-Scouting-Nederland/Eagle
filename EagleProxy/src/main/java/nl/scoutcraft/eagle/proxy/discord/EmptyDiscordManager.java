package nl.scoutcraft.eagle.proxy.discord;

import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public class EmptyDiscordManager implements IDiscordManager {

    @Override
    public void start() {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void addReadyAction(Runnable action) {
    }

    @Override
    public void onJoin(Player player) {
    }

    @Override
    public void onQuit(UUID playerId) {
    }

    @Override
    public void sendMessage(String userId, String message, Consumer<String> idAction) {
    }

    @Override
    public void sendMessage(String userId, DiscordEmbed embed, Consumer<String> idAction) {
    }

    @Override
    public void editMessage(String userId, String messageId, String message) {
    }

    @Override
    public void editMessage(String userId, String embedId, DiscordEmbed embed) {
    }

    @Override
    public void sendChannelMessage(DiscordChannel channel, String message, Consumer<String> idAction) {
    }

    @Override
    public void sendChannelMessage(DiscordChannel channel, DiscordEmbed embed, Consumer<String> idAction) {
    }

    @Override
    public void editChannelMessage(DiscordChannel channel, String messageId, String message) {
    }

    @Override
    public void editChannelMessage(DiscordChannel channel, String embedId, DiscordEmbed embed) {
    }

    @Override
    public void updateDiscordNameAndRoles(Player player) {
    }

    @Nullable
    @Override
    public String[] removeToken(String token) {
        return null;
    }
}
