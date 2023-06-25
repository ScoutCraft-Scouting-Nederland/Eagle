package nl.scoutcraft.eagle.server.player;

import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.IPlayerChannel;
import nl.scoutcraft.eagle.libs.player.PlayerData;
import nl.scoutcraft.eagle.libs.player.PlayerLocationInfo;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import nl.scoutcraft.eagle.server.network.ServerNetworkChannel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerManager implements IPlayerChannel {

    private final EagleServer plugin;
    private final Map<UUID, Location> gtps;
    private final NetworkChannel<IPlayerChannel> channel;

    public PlayerManager(EagleServer plugin) {
        this.plugin = plugin;
        this.gtps = Collections.synchronizedMap(new HashMap<>());
        this.channel = new ServerNetworkChannel<>("eagle:player", IPlayerChannel.class, this, plugin);
    }

    public void onJoin(Player player) {
        this.requestData(player.getUniqueId(), 0);
        this.requestIgnoredByList(player.getUniqueId(), 0);
        Optional.ofNullable(this.gtps.remove(player.getUniqueId())).ifPresent(player::teleport);
    }

    private void requestData(UUID playerId, int attempt) {
        if (attempt > 0)
            this.plugin.getLogger().warning("getData request timed out (player=" + playerId + ")");

        if (attempt > 5) {
            this.plugin.getLogger().warning("getData request has now timed out 5 times, and will no longer continue! (player=" + playerId + ")");
            return;
        }

        this.channel.<PlayerData>request()
                .onResponse(this::setData)
                .setTimeout(1500)
                .onTimeout(() -> this.requestData(playerId, attempt + 1))
                .setTarget(Bukkit.getServer())
                .getData(playerId);
    }

    private void setData(PlayerData data) {
        if (data == null) return;
        this.setPrefix(data.getIdentifier(), data.getPrefix());
        this.setDisplayName(data.getIdentifier(), data.getDisplayName());
        this.setLocale(data.getIdentifier(), data.getLocale());
    }

    private void requestIgnoredByList(UUID playerId, int attempt) {
        if (attempt > 2)
            return;

        this.channel.<List<String>>request()
                .onResponse(list -> this.setIgnoredByList(playerId, list))
                .setTimeout(3000)
                .onTimeout(() -> this.requestIgnoredByList(playerId, attempt + 1))
                .setTarget(Bukkit.getServer())
                .getIgnoredByList(playerId);
    }

    private void setIgnoredByList(UUID playerId, List<String> list) {
        List<UUID> ignoredBy = new ArrayList<>();
        list.forEach(uuid -> ignoredBy.add(UUID.fromString(uuid)));

        Optional.ofNullable(Bukkit.getPlayer(playerId)).ifPresent(p -> Bukkit.getScheduler().runTask(this.plugin, () -> this.setDataValue(p.getPersistentDataContainer(), EagleKeys.IGNORED_BY, EagleKeys.UUID_LIST_TAG_TYPE, ignoredBy)));
    }

    @Override
    public void setPrefix(UUID playerId, @Nullable String prefix) {
        Optional.ofNullable(Bukkit.getPlayer(playerId)).ifPresent(p -> Bukkit.getScheduler().runTask(this.plugin, () -> this.setDataValue(p.getPersistentDataContainer(), EagleKeys.PREFIX, PersistentDataType.STRING, prefix)));
    }

    @Override
    public void setDisplayName(UUID playerId, @Nullable String displayName) {
        Optional.ofNullable(Bukkit.getPlayer(playerId)).ifPresent(p -> Bukkit.getScheduler().runTask(this.plugin, () -> this.setDataValue(p.getPersistentDataContainer(), EagleKeys.DISPLAY_NAME, PersistentDataType.STRING, displayName)));
    }

    @Override
    public void setLocale(UUID playerId, @Nullable Locale locale) {
        Optional.ofNullable(Bukkit.getPlayer(playerId)).ifPresent(p -> Bukkit.getScheduler().runTask(this.plugin, () -> this.setDataValue(p.getPersistentDataContainer(), EagleKeys.LOCALE, EagleKeys.LOCALE_TAG_TYPE, locale)));
    }

    private <T> void setDataValue(PersistentDataContainer container, NamespacedKey key, PersistentDataType<?, T> type, T value) {
        if (value == null) container.remove(key);
        else container.set(key, type, value);
    }

    @Override
    public void updateIgnoredBy(UUID playerId, UUID ignoredBy, boolean state) {
        Player player = Bukkit.getPlayer(playerId);
        if (player == null)
            return;

        Bukkit.getScheduler().runTask(this.plugin, () -> {
            List<UUID> list = player.getPersistentDataContainer().getOrDefault(EagleKeys.IGNORED_BY, EagleKeys.UUID_LIST_TAG_TYPE, new ArrayList<>(state ? 1 : 0));
            if (state) list.add(ignoredBy);
            else list.remove(ignoredBy);

            player.getPersistentDataContainer().set(EagleKeys.IGNORED_BY, EagleKeys.UUID_LIST_TAG_TYPE, list);
        });
    }

    @Override
    public void teleport(UUID playerId, UUID targetId) {
        Player target = Bukkit.getPlayer(targetId);
        if (target == null) return;

        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            Bukkit.getScheduler().runTask(EagleServer.getInstance(), () -> player.teleport(target.getLocation()));
        } else {
            this.gtps.put(playerId, target.getLocation());
            Bukkit.getScheduler().runTaskLater(EagleServer.getInstance(), () -> this.gtps.remove(playerId), 100);
        }
    }

    @Override
    public PlayerLocationInfo getLocation(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid))
                .map(Entity::getLocation)
                .map(l -> new PlayerLocationInfo(uuid, l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld().getName()))
                .orElse(null);
    }
}
