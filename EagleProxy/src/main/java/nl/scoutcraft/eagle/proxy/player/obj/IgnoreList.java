package nl.scoutcraft.eagle.proxy.player.obj;

import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The ignoree is never removed from the infoCache, because there could be multiple players that ignored the same player.
 * This does introduce a memory leak.
 */
public class IgnoreList {

    private final EagleProxy plugin;

    private final Map<UUID, List<UUID>> ignores;
    private final Map<UUID, PlayerInfo> infoCache;

    public IgnoreList(EagleProxy plugin) {
        this.plugin = plugin;
        this.ignores = new HashMap<>();
        this.infoCache = new HashMap<>();
    }

    public void load(UUID uuid) {
        this.plugin.getSQLManager().getIgnores(uuid).forEach(info -> this.addIgnoreUnsafe(uuid, info));
    }

    public void unload(UUID uuid) {
        this.ignores.remove(uuid);
    }

    @Nullable
    public PlayerInfo getPlayerInfo(String name) {
        return this.infoCache.values().stream().filter(info -> info.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<PlayerInfo> getIgnores(UUID ignorer) {
        return this.ignores.computeIfAbsent(ignorer, k -> new ArrayList<>()).stream().map(this.infoCache::get).collect(Collectors.toList());
    }

    @Nullable
    public PlayerInfo getIgnore(UUID ignorer, UUID ignoree) {
        return this.isIgnoring(ignorer, ignoree) ? this.infoCache.get(ignoree) : null;
    }

    public boolean isIgnoring(UUID ignorer, UUID ignoree) {
        return this.ignores.computeIfAbsent(ignorer, k -> new ArrayList<>()).contains(ignoree);
    }

    public void addIgnore(UUID ignorer, PlayerInfo ignoree, LocalDateTime timestamp) {
        this.addIgnoreUnsafe(ignorer, ignoree);

        EagleProxy.getProxy().getPlayer(ignoree.getUniqueId())
                .ifPresent(player -> this.plugin.getPlayerManager().sendIgnoredByUpdate(player, ignorer, true));

        this.plugin.getSQLManager().ignore(ignorer, ignoree.getUniqueId(), timestamp);
    }

    public void removeIgnore(UUID ignorer, UUID ignoree) {
        Optional.ofNullable(this.ignores.get(ignorer))
                .ifPresent(l -> l.remove(ignoree));

        EagleProxy.getProxy().getPlayer(ignoree)
                .ifPresent(player -> this.plugin.getPlayerManager().sendIgnoredByUpdate(player, ignorer, false));

        this.plugin.getSQLManager().unignore(ignorer, ignoree);
    }

    private void addIgnoreUnsafe(UUID ignorer, PlayerInfo ignoree) {
        this.ignores.computeIfAbsent(ignorer, k -> new ArrayList<>()).add(ignoree.getUniqueId());
        this.infoCache.put(ignoree.getUniqueId(), ignoree);
    }
}
