package nl.scoutcraft.eagle.proxy.discord;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.scotty.Scotty;
import nl.scoutcraft.eagle.scotty.discord.EmbedTypes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public class DiscordManager implements IDiscordManager {

    private final nl.scoutcraft.eagle.scotty.discord.DiscordManager manager;

    public DiscordManager() {
        this.manager = Scotty.getInstance().getDiscordManager();
        this.manager.registerChecker(userId -> {
            PlayerInfo playerInfo = EagleProxy.getInstance().getSQLManager().getDiscordPlayerInfo(userId).orElse(null);
            if (playerInfo != null)
                return CommandMessages.REGISTER_DISCORD_ALREADY_REGISTERED.getString(EagleProxy.getProxy().getPlayer(playerInfo.getUniqueId()).orElse(null));

            return null;
        });
    }

    @Override
    public void start() {
        this.manager.start();
    }

    @Override
    public void close() {
        this.manager.close();
    }

    @Override
    public boolean isReady() {
        return this.manager.isReady();
    }

    @Override
    public void addReadyAction(Runnable action) {
        this.manager.addReadyAction(action);
    }

    @Override
    public void onJoin(Player player) {
        EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player).getDiscordId().ifPresent(id -> this.manager.onJoin(player, id));
    }

    @Override
    public void onQuit(UUID playerId) {
        this.manager.onQuit(playerId);
    }

    @Override
    public void sendMessage(String userId, String message, Consumer<String> idAction) {
        this.manager.sendMessage(userId, message, idAction);
    }

    @Override
    public void sendMessage(String userId, DiscordEmbed embed, Consumer<String> idAction) {
        this.manager.sendMessage(userId, this.fromEagleEmbed(embed), idAction);
    }

    @Override
    public void editMessage(String userId, String messageId, String message) {
        this.manager.editMessage(userId, messageId, message);
    }

    @Override
    public void editMessage(String userId, String embedId, DiscordEmbed embed) {
        this.manager.editMessage(userId, embedId, this.fromEagleEmbed(embed));
    }

    @Override
    public void sendChannelMessage(DiscordChannel channel, String message, Consumer<String> idAction) {
        this.manager.sendChannelMessage(nl.scoutcraft.eagle.scotty.discord.DiscordChannel.of(channel.getId()), message, idAction);
    }

    @Override
    public void sendChannelMessage(DiscordChannel channel, DiscordEmbed embed, Consumer<String> idAction) {
        this.manager.sendChannelMessage(nl.scoutcraft.eagle.scotty.discord.DiscordChannel.of(channel.getId()), this.fromEagleEmbed(embed), idAction);
    }

    @Override
    public void editChannelMessage(DiscordChannel channel, String messageId, String message) {
        this.manager.editChannelMessage(nl.scoutcraft.eagle.scotty.discord.DiscordChannel.of(channel.getId()), messageId, message);
    }

    @Override
    public void editChannelMessage(DiscordChannel channel, String embedId, DiscordEmbed embed) {
        this.manager.editChannelMessage(nl.scoutcraft.eagle.scotty.discord.DiscordChannel.of(channel.getId()), embedId, this.fromEagleEmbed(embed));
    }

    @Override
    public void updateDiscordNameAndRoles(Player player) {
        EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player).getDiscordId().ifPresent(id -> this.manager.updateDiscordNameAndRoles(player, id));
    }

    private nl.scoutcraft.eagle.scotty.discord.Embed fromEagleEmbed(DiscordEmbed embed) {
        return new nl.scoutcraft.eagle.scotty.discord.Embed(EmbedTypes.of(embed.type().getId()), embed.params());
    }

    @Nullable
    @Override
    public String[] removeToken(String token) {
        return this.manager.removeToken(token);
    }
}
