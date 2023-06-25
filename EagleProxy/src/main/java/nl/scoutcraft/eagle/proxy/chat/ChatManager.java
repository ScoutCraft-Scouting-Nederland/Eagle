package nl.scoutcraft.eagle.proxy.chat;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.IChatChannel;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.discord.DiscordChannel;
import nl.scoutcraft.eagle.proxy.event.ConfigReloadEvent;
import nl.scoutcraft.eagle.proxy.io.ConfigAdaptor;
import nl.scoutcraft.eagle.proxy.io.ProxyNetworkChannel;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.GeneralMessages;
import nl.scoutcraft.eagle.proxy.player.obj.IgnoreList;
import nl.scoutcraft.eagle.proxy.utils.Task;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class ChatManager extends Task implements IChatChannel {

    private static final Pattern URL_PATTERN = Pattern.compile("(http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?");

    private final EagleProxy plugin;
    private final Map<UUID, Integer> chatCounter;
    private final Set<String> chatLocks;
    private final Map<UUID, BiConsumer<Player, String>> actions;
    private final IgnoreList ignores;
    private final NetworkChannel<IChatChannel> chatChannel;

    private boolean globalChatLock;

    private String chatFormat;
    private int capsCharacters;
    private float capsPercentage;
    private int chatSpamThreshold;

    public ChatManager(EagleProxy plugin) {
        super(2, 2, TimeUnit.SECONDS);
        this.plugin = plugin;
        this.chatCounter = Collections.synchronizedMap(new HashMap<>());
        this.actions = new HashMap<>();
        this.ignores = new IgnoreList(plugin);
        this.chatLocks = new HashSet<>();
        this.chatChannel = new ProxyNetworkChannel<>("eagle:chat", IChatChannel.class, this, plugin);
        this.chatFormat = "&7[%prefix%&7] %1$s » &r%2$s";
    }

    @Override
    public void run() {
        this.chatCounter.values().removeIf(v -> v <= 1);
        this.chatCounter.keySet().forEach(k -> this.chatCounter.computeIfPresent(k, (u, v) -> v - 1));
    }

    public void load(ConfigAdaptor config) {
        super.setInterval(config.getNode("chat", "spam", "millis").getInt(2500), TimeUnit.MILLISECONDS);
        super.start();

        this.chatFormat = config.getNode("chat", "format").getString();
        this.capsCharacters = config.getNode("chat", "caps", "min_characters").getInt(4);
        this.capsPercentage = config.getNode("chat", "caps", "min_percentage").getFloat(50) / 100F;
        this.chatSpamThreshold = config.getNode("chat", "spam", "threshold").getInt(3);
    }

    public void onJoin(UUID id) {
        this.ignores.load(id);
    }

    public void onQuit(UUID id) {
        this.ignores.unload(id);
        this.actions.remove(id);
    }

    @Subscribe
    public void onReload(ConfigReloadEvent event) {
        this.load(event.getConfig());
    }

    /*
     * Formatting chat colors proxy-side does not work with chat message.
     */
    @Subscribe
    public void onChat(PlayerChatEvent event) {
        if (event.getMessage().startsWith("/")) return;
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        // Chat Actions
        BiConsumer<Player, String> action = this.actions.remove(player.getUniqueId());
        if (action != null) {
            action.accept(player, message);
            event.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        // ChatLock
        if ((this.globalChatLock || this.chatLocks.contains(player.getCurrentServer().map(server -> server.getServerInfo().getName()).orElse("invalid"))) && !player.hasPermission(Perms.CHAT_LOCK_BYPASS)) {
            CommandMessages.CHATLOCK_CHAT_IS_LOCKED.send(player);
            event.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        // Caps limiting
        if (message.length() > this.capsCharacters && !player.hasPermission(Perms.CHAT_CAPS_BYPASS)) {
            float ups = 0;
            for (char c : message.toCharArray())
                if (Character.isUpperCase(c))
                    ups++;

            float capsPercentage = ups / ((float) message.replace(" ", "").length());
            if (capsPercentage > this.capsPercentage) {
                GeneralMessages.CHAT_CAPS.send(player);
                event.setResult(PlayerChatEvent.ChatResult.denied());
                return;
            }
        }

        // Spam limiting
        if (!player.hasPermission(Perms.CHAT_SPAM_BYPASS)) {
            Integer counter = this.chatCounter.getOrDefault(player.getUniqueId(), 0);
            if (counter >= this.chatSpamThreshold) {
                GeneralMessages.CHAT_SPAMMING.send(player);
                event.setResult(PlayerChatEvent.ChatResult.denied());
                return;
            }

            this.chatCounter.put(player.getUniqueId(), counter + 1);
        }

        // Link limiting
        if (!player.hasPermission(Perms.CHAT_LINK_BYPASS) && URL_PATTERN.matcher(message).find()) {
            GeneralMessages.CHAT_LINK.send(player);
            event.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        // Eagle Logging
        this.plugin.getSQLManager().addChatMessage(player, message);
        this.plugin.getDiscordManager().sendChannelMessage(DiscordChannel.CHATLOG, "**[" + player.getCurrentServer().map(server -> server.getServerInfo().getName()).orElse("invalid") + "]** " + player.getUsername() + " **»** " + message);

        // Chat Channels
        ChatChannels.getFromChatMessage(player, message).ifPresent(channel -> {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            channel.sendChat(player, message);
        });
    }

    public void enableGlobalChatLock() {
        this.globalChatLock = true;
    }

    public void disableGlobalChatLock() {
        this.globalChatLock = false;
    }

    public void enableChatLock(String serverName) {
        this.chatLocks.add(serverName);
    }

    public void disableChatlock(String serverName) {
        this.chatLocks.remove(serverName);
    }

    public void requestChatMessage(UUID playerId, BiConsumer<Player, String> action) {
        this.actions.put(playerId, action);
    }

    public IgnoreList getIgnores() {
        return this.ignores;
    }

    public boolean isChatLocked(String name) {
        return this.chatLocks.contains(name);
    }

    public boolean isGlobalChatLock() {
        return this.globalChatLock;
    }

    @Override
    public String getChatFormat() {
        return this.chatFormat;
    }
}
