package nl.scoutcraft.eagle.proxy.server;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.event.ConfigReloadEvent;
import nl.scoutcraft.eagle.proxy.io.ConfigAdaptor;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;

public class ProxyManager {

    private final EagleProxy plugin;
    private final MaintenanceListener listener;

    private boolean maintenance = false;

    private Component maintenanceMotd;
    private Component[] motds;
    private int index;

    public ProxyManager(EagleProxy plugin) {
        this.plugin = plugin;
        this.listener = new MaintenanceListener();

        this.setMotds(plugin.getConfigAdaptor());
    }

    public boolean isMaintenanceMode() {
        return this.maintenance;
    }

    public void setMaintenanceMode(boolean value) {
        if (this.maintenance == value) return;
        this.maintenance = value;

        IPlaceholder placeholder = new MessagePlaceholder("%state%", value ? CommandMessages.ENABLED : CommandMessages.DISABLED);
        ProxyServer proxy = EagleProxy.getProxy();

        if (value) {
            proxy.getAllPlayers().forEach(player -> {
                if (player.hasPermission(Perms.MAINTENANCE_BYPASS))
                    CommandMessages.MAINTENANCE_BROADCAST.send(player, placeholder);
                else player.disconnect(CommandMessages.MAINTENANCE_KICK.get(player));
            });
            proxy.getEventManager().register(this.plugin, this.listener);
        } else {
            proxy.getAllPlayers().forEach(player -> CommandMessages.MAINTENANCE_BROADCAST.send(player, placeholder));
            proxy.getEventManager().unregisterListener(this.plugin, this.listener);
        }
    }

    @Subscribe(order = PostOrder.LAST)
    public void onPing(ProxyPingEvent event) {
        if (this.maintenance) {
            event.setPing(event.getPing().asBuilder().description(this.maintenanceMotd).build());
            return;
        }

        if (this.motds.length == 0) return;

        this.index = this.index + 1 >= this.motds.length ? 0 : this.index + 1;
        event.setPing(event.getPing().asBuilder().description(this.motds[this.index]).build());
    }

    @Subscribe
    public void onConfigReload(ConfigReloadEvent event) {
        this.setMotds(event.getConfig());
    }

    private void setMotds(ConfigAdaptor config) {
        this.maintenanceMotd = TextUtils.colorize(config.getNode("maintenance-motd").getString("").replace("\\n", "\n"));
        this.index = 0;

        try {
            this.motds = config.getNode("motd").getList(String.class, Collections.emptyList()).stream().map(s -> s.replace("\\n", "\n")).map(TextUtils::colorize).toArray(Component[]::new);
        } catch (SerializationException exc) {
            EagleProxy.getInstance().getLogger().error("Failed to load motd's from config", exc);
        }
    }

    public static class MaintenanceListener {

        @Subscribe
        public void onLogin(PostLoginEvent event) {
            if (!event.getPlayer().hasPermission(Perms.MAINTENANCE_BYPASS))
                event.getPlayer().disconnect(CommandMessages.MAINTENANCE_KICK.get(event.getPlayer()));
        }
    }
}
