package nl.scoutcraft.eagle.proxy.player.obj;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.ICommandsChannel;
import nl.scoutcraft.eagle.libs.network.channels.IPlayerChannel;
import nl.scoutcraft.eagle.libs.player.PlayerData;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.event.ConfigReloadEvent;
import nl.scoutcraft.eagle.proxy.io.ProxyNetworkChannel;
import nl.scoutcraft.eagle.proxy.player.tasks.AnnouncerTask;
import nl.scoutcraft.eagle.proxy.player.tasks.PlaytimeTask;
import nl.scoutcraft.eagle.proxy.player.tasks.StaffAnnouncerTask;
import nl.scoutcraft.eagle.proxy.utils.TextureUtils;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlayerManager implements IPlayerChannel, ICommandsChannel {

    private final EagleProxy plugin;
    private final Map<UUID, ScoutPlayer> scoutPlayers;
    private final Set<String> names;

    private final NetworkChannel<IPlayerChannel> playerChannel;
    private final NetworkChannel<ICommandsChannel> commandsChannel;

    private PlaytimeTask playtimeTask;
    private AnnouncerTask announcerTask;
    private StaffAnnouncerTask staffAnnouncerTask;

    public PlayerManager(EagleProxy plugin) {
        this.plugin = plugin;
        this.scoutPlayers = new HashMap<>();
        this.names = new HashSet<>();
        this.playerChannel = new ProxyNetworkChannel<>("eagle:player", IPlayerChannel.class, this, plugin);
        this.commandsChannel = new ProxyNetworkChannel<>("eagle:commands", ICommandsChannel.class, this, plugin);

        this.onConfigReload(null);
    }

    public NetworkChannel<IPlayerChannel> getPlayerChannel() {
        return this.playerChannel;
    }

    public NetworkChannel<ICommandsChannel> getCommandsChannel() {
        return this.commandsChannel;
    }

    public ScoutPlayer getScoutPlayer(Player player) {
        if (player == null) return null;

        return this.scoutPlayers.get(player.getUniqueId());
    }

    @Nullable
    public ScoutPlayer getScoutPlayer(UUID uuid) {
        if (uuid == null) return null;

        return this.scoutPlayers.get(uuid);
    }

    public Collection<ScoutPlayer> getScoutPlayers() {
        return this.scoutPlayers.values();
    }

    @Subscribe
    public void onConfigReload(@Nullable ConfigReloadEvent event) {
        if (this.playtimeTask != null)
            this.playtimeTask.stop();
        if (this.announcerTask != null)
            this.announcerTask.stop();
        if (this.staffAnnouncerTask != null)
            this.staffAnnouncerTask.stop();

        this.playtimeTask = new PlaytimeTask(this.plugin);
        this.announcerTask = new AnnouncerTask(this.plugin);
        this.staffAnnouncerTask = new StaffAnnouncerTask(this.plugin);

        this.playtimeTask.start();
        this.announcerTask.start();
        this.staffAnnouncerTask.start();
    }

    public Set<String> getNames() {
        return this.names;
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        final LocalDateTime timestamp = LocalDateTime.now();
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        // This block is synchronized so PlayerManager#unload can't be executed during this block. Prevents unloading during login
        synchronized (this.scoutPlayers) {
            if (this.scoutPlayers.containsKey(uuid)) {
                this.scoutPlayers.get(uuid).setLoaded(true);
                return;
            }

            this.scoutPlayers.put(uuid, new ScoutPlayer(uuid, timestamp));

            this.names.add(player.getUsername());
        }

        EagleProxy.getProxy().getScheduler()
                .buildTask(this.plugin, () -> this.onLogin(player, timestamp))
                .delay(25, TimeUnit.MILLISECONDS)
                .schedule();
        EagleProxy.getProxy().getScheduler()
                .buildTask(this.plugin, () -> this.plugin.getDiscordManager().onJoin(player))
                .delay(250, TimeUnit.MILLISECONDS)
                .schedule();
        EagleProxy.getProxy().getScheduler()
                .buildTask(this.plugin, () -> StaffAnnouncerTask.sendInfo(player))
                .delay(5, TimeUnit.SECONDS)
                .schedule();
    }

    private void onLogin(Player player, LocalDateTime timestamp) {
        synchronized (this.scoutPlayers) {
            ScoutPlayer sp = this.getScoutPlayer(player);
            if (sp == null || !player.isActive() || !sp.isLoaded())
                return;

            this.plugin.getSQLManager().startSession(sp, player, timestamp);
            this.plugin.getChatManager().onJoin(player.getUniqueId());
            sp.setTextureProperty(TextureUtils.requestProfile(player.getUniqueId()));
        }
    }

    @Subscribe
    public void onQuit(DisconnectEvent event) {
        final LocalDateTime timestamp = LocalDateTime.now();
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final String username = player.getUsername();
        final UUID sessionId;

        synchronized (this.scoutPlayers) {
            final ScoutPlayer sp = this.getScoutPlayer(playerId);
            if (sp == null)
                return;

            this.names.remove(player.getUsername());
            sp.getDisplayName().ifPresent(this.names::remove);
            sp.setLoaded(false);

            sessionId = sp.getSessionId();
        }

        EagleProxy.getProxy().getScheduler()
                .buildTask(this.plugin, () -> this.unload(sessionId, playerId, username, timestamp))
                .delay(5, TimeUnit.SECONDS)
                .schedule();
    }

    private void unload(UUID sessionId, UUID playerId, String username, LocalDateTime timestamp) {

        // This block is synchronized so this cannot be executed while a login is also being executed.
        synchronized (this.scoutPlayers) {
            ScoutPlayer sp = this.scoutPlayers.get(playerId);
            if (sp != null && !sp.isLoaded()) {
                this.scoutPlayers.remove(playerId);

                this.plugin.getSQLManager().stopSession(sessionId, playerId, timestamp);
                this.plugin.getChatManager().onQuit(playerId);
                this.plugin.getDiscordManager().onQuit(playerId);
                this.plugin.getPartyManager().onQuit(playerId, username);
            }
        }
    }

    public void sendIgnoredByUpdate(Player player, UUID uuid, boolean state) {
        this.playerChannel.request().setTarget(player).updateIgnoredBy(player.getUniqueId(), uuid, state);
    }

    public void setSpying(Player player, boolean value) {
        this.getScoutPlayer(player).setSpying(value);

        this.plugin.getSQLManager().setSpying(player.getUniqueId(), value);
    }

    public void setPrefix(Player player, @Nullable String prefix) {
        this.getScoutPlayer(player).setPrefix(prefix);
        this.playerChannel.request().setTarget(player).setPrefix(player.getUniqueId(), prefix);
    }

    public void setDisplayName(Player player, @Nullable String displayName) {
        this.getScoutPlayer(player).setDisplayName(displayName);
        this.playerChannel.request().setTarget(player).setDisplayName(player.getUniqueId(), displayName);
        this.plugin.getSQLManager().setDisplayName(player.getUniqueId(), displayName);
    }

    public void setLocale(Player player, @Nullable Locale locale) {
        this.getScoutPlayer(player).setLocale(locale);
        this.playerChannel.request().setTarget(player).setLocale(player.getUniqueId(), locale);
        this.plugin.getSQLManager().setLocale(player.getUniqueId(), locale);
    }

    @Override
    public PlayerData getData(UUID playerId) {
        ScoutPlayer scoutPlayer = this.getScoutPlayer(playerId);

        return scoutPlayer == null ? null : new PlayerData(playerId, scoutPlayer.getPrefix(), scoutPlayer.getDisplayName().orElse(null), scoutPlayer.getLocale().orElse(null));
    }

    @Override
    public List<String> getIgnoredByList(UUID playerId) {
        return this.plugin.getSQLManager().getIgnoredByList(playerId);
    }

    @Override
    public String getTextureProperty(UUID playerId) {
        ScoutPlayer scoutPlayer = this.getScoutPlayer(playerId);

        return scoutPlayer == null ? null : scoutPlayer.getTextureProperty().orElse(null);
    }

    @Override
    public void setInGame(UUID playerId, boolean state) {
        ScoutPlayer scoutPlayer = this.getScoutPlayer(playerId);
        if (scoutPlayer != null)
            scoutPlayer.setInGame(state);
    }

    @Override
    public void executeChatMessageCommand(UUID playerId, String command, String placeholderKey) {
        this.plugin.getChatManager().requestChatMessage(playerId, (player, text) -> {
            EagleProxy.getProxy().getCommandManager().executeImmediatelyAsync(player, command.replace(placeholderKey, text));
        });
    }

    @Override
    public void executeCommand(UUID playerId, String command) {
        ProxyServer proxy = EagleProxy.getProxy();
        proxy.getPlayer(playerId).ifPresent(player -> proxy.getCommandManager().executeImmediatelyAsync(player, command));
    }

    @Override
    public void executeCommand(String command) {
        EagleProxy.getProxy().getCommandManager().executeImmediatelyAsync(EagleProxy.getProxy().getConsoleCommandSource(), command);
    }
}
