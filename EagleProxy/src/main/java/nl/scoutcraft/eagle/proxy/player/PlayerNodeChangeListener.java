package nl.scoutcraft.eagle.proxy.player;

import com.velocitypowered.api.proxy.Player;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;
import nl.scoutcraft.eagle.proxy.EagleProxy;

import java.util.Optional;
import java.util.UUID;

public class PlayerNodeChangeListener {

    private final EagleProxy plugin;
    private final LuckPerms luckPerms;

    public PlayerNodeChangeListener(EagleProxy plugin) {
        this.plugin = plugin;
        this.luckPerms = LuckPermsProvider.get();
    }

    public void register() {
        this.luckPerms.getEventBus().subscribe(this.plugin, UserDataRecalculateEvent.class, this::onLuckPing);
    }

    private void onLuckPing(UserDataRecalculateEvent event) {
        User user = event.getUser();
        UUID uuid = user.getUniqueId();
        Optional<Player> player = EagleProxy.getProxy().getPlayer(uuid);

        // Fixes an issue where this event is sometimes thrown before a player has fully connected to a server
        if (player.isEmpty() || player.get().getCurrentServer().isEmpty())
            return;

        String prefix = user.getCachedData().getMetaData().getPrefix();
        if (prefix == null)
            return;

        EagleProxy.getProxy().getScheduler().buildTask(this.plugin, () -> this.plugin.getPlayerManager().setPrefix(player.get(), prefix)).schedule();
    }
}
