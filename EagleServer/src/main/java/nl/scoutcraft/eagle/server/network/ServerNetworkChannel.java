package nl.scoutcraft.eagle.server.network;

import com.google.common.io.ByteArrayDataOutput;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerNetworkChannel<T> extends NetworkChannel<T> implements PluginMessageListener {

    private final JavaPlugin plugin;

    public ServerNetworkChannel(String name, Class<T> interfaceClass, @Nullable T implementation, JavaPlugin plugin) {
        super(name, interfaceClass, implementation, plugin);
        this.plugin = plugin;
    }

    @Override
    protected void register(Object plugin) {
        JavaPlugin p = (JavaPlugin) plugin;
        Bukkit.getMessenger().registerOutgoingPluginChannel(p, super.name);
        Bukkit.getMessenger().registerIncomingPluginChannel(p, super.name, this);
    }

    @Override
    protected void sendData(Object conn, ByteArrayDataOutput output) {
        if (conn instanceof PluginMessageRecipient)
            ((PluginMessageRecipient) conn).sendPluginMessage(this.plugin, super.name, output.toByteArray());
    }

    @Override
    protected void scheduleAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, task);
    }

    @Override
    protected void scheduleAsync(Runnable task, long millis) {
        Bukkit.getScheduler().runTaskLater(this.plugin, task, millis / 50);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String tag, @NotNull Player player, @NotNull byte[] data) {
        super.handlePluginMessage(tag, player, data);
    }
}
