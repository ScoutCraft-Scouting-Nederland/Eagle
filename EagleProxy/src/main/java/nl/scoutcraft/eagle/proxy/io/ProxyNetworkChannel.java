package nl.scoutcraft.eagle.proxy.io;

import com.google.common.io.ByteArrayDataOutput;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class ProxyNetworkChannel<T> extends NetworkChannel<T> {

    private final Object plugin;

    private ChannelIdentifier identifier;

    public ProxyNetworkChannel(String name, Class<T> interfaceClass, @Nullable T implementation, Object plugin) {
        super(name, interfaceClass, implementation, plugin);

        this.plugin = plugin;
        this.identifier = null;
    }

    @Override
    protected void register(Object plugin) {
        EagleProxy.getProxy().getChannelRegistrar().register(this.getIdentifier());
        EagleProxy.getProxy().getEventManager().register(plugin, this);
    }

    @Override
    protected void sendData(Object conn, ByteArrayDataOutput output) {
        if (conn instanceof Player) ((Player) conn).getCurrentServer().ifPresent(server -> server.sendPluginMessage(this.getIdentifier(), output.toByteArray()));
        else if (conn instanceof ServerConnection) ((ServerConnection) conn).sendPluginMessage(this.getIdentifier(), output.toByteArray());
        else if (conn instanceof RegisteredServer) ((RegisteredServer) conn).sendPluginMessage(this.getIdentifier(), output.toByteArray());
        else if (conn instanceof ScoutServer) ((ScoutServer) conn).getServer().sendPluginMessage(this.getIdentifier(), output.toByteArray());
    }

    @Override
    protected void scheduleAsync(Runnable task) {
        task.run();
    }

    @Override
    protected void scheduleAsync(Runnable task, long millis) {
        EagleProxy.getProxy().getScheduler().buildTask(this.plugin, task).delay(millis, TimeUnit.MILLISECONDS);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        this.handlePluginMessage(event.getIdentifier().getId(), event.getSource(), event.getData());
    }

    private ChannelIdentifier getIdentifier() {
        if (this.identifier == null)
            this.identifier = MinecraftChannelIdentifier.create("eagle", super.name.split(":")[1]);
        return this.identifier;
    }
}
