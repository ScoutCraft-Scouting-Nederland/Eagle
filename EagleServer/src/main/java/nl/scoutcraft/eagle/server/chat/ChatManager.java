package nl.scoutcraft.eagle.server.chat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.IChatChannel;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import nl.scoutcraft.eagle.server.network.ServerNetworkChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Consumer;

public class ChatManager implements IChatManager {

    private final Map<UUID, Consumer<String>> actions;
    private final NetworkChannel<IChatChannel> chatChannel;

    private ChatRenderer renderer;
    private boolean isDefaultRenderer;

    public ChatManager(EagleServer plugin) {
        this.actions = new HashMap<>();
        this.chatChannel = new ServerNetworkChannel<>("eagle:chat", IChatChannel.class, null, plugin);
        this.renderer = this.createRenderer("&7%1$s &7» &r%2$s");
        this.isDefaultRenderer = true;
    }

    @Override
    public void requestChatMessage(Player player, Consumer<String> action) {
        this.actions.put(player.getUniqueId(), action);
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        // Chat Actions
        Consumer<String> action = this.actions.remove(player.getUniqueId());
        if (action != null) {
            action.accept(TextUtils.toString(event.message()));
            event.setCancelled(true);
            return;
        }

        // Renderer
        event.renderer(this.getRenderer());
        if (player.hasPermission(Perms.CHAT_COLORED))
            event.message(TextUtils.colorize(event.message()));

        // Ignores
        PersistentDataContainer data = player.getPersistentDataContainer();
        List<UUID> ignoredBy = data.getOrDefault(EagleKeys.IGNORED_BY, EagleKeys.UUID_LIST_TAG_TYPE, new ArrayList<>(0));

        event.viewers().removeIf(v -> v instanceof Player && ignoredBy.contains(((Player) v).getUniqueId()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.getRenderer();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.actions.remove(event.getPlayer().getUniqueId());
    }

    public ChatRenderer getRenderer() {
        if (this.isDefaultRenderer)
            this.requestFormat(2);

        return this.renderer;
    }

    private void requestFormat(int repeat) {
        this.chatChannel.<String>request()
                .onResponse(this::setFormat)
                .onTimeout(() -> { if (repeat > 0) requestFormat(repeat - 1); })
                .setTarget(Bukkit.getServer())
                .getChatFormat();
    }

    private void setFormat(String format) {
        this.renderer = this.createRenderer(format);
        this.isDefaultRenderer = false;
    }

    private ChatRenderer createRenderer(String format) {
        return (player, displayName, message, audience) -> {
            PersistentDataContainer data = player.getPersistentDataContainer();
            String prefix = data.getOrDefault(EagleKeys.PREFIX, PersistentDataType.STRING, "");
            String name = data.getOrDefault(EagleKeys.DISPLAY_NAME, PersistentDataType.STRING, player.getName());

            return TextUtils.colorize(format.replace("%prefix%", prefix).replace("%1$s", name).replace("%2$s", "")).append(message);
        };
    }
}

