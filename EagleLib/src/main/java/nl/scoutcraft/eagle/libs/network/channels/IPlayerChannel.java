package nl.scoutcraft.eagle.libs.network.channels;

import nl.scoutcraft.eagle.libs.network.ProxySide;
import nl.scoutcraft.eagle.libs.network.ServerSide;
import nl.scoutcraft.eagle.libs.player.PlayerData;
import nl.scoutcraft.eagle.libs.player.PlayerLocationInfo;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface IPlayerChannel {

    @ProxySide
    default PlayerData getData(UUID playerId) { return null; }

    @ProxySide
    default List<String> getIgnoredByList(UUID playerId) { return Collections.emptyList(); }

    @ProxySide
    default String getTextureProperty(UUID playerId) { return null; }

    @ProxySide
    default void setInGame(UUID playerId, boolean state) {}

    @ServerSide
    default void setPrefix(UUID playerId, String prefix) {}

    @ServerSide
    default void setDisplayName(UUID playerId, String displayName) {}

    @ServerSide
    default void setLocale(UUID playerId, Locale locale) {}

    @ServerSide
    default void updateIgnoredBy(UUID playerId, UUID ignoredBy, boolean state) {}

    @ServerSide
    default void teleport(UUID playerId, UUID targetId) {}

    @ServerSide
    default PlayerLocationInfo getLocation(UUID playerId) { return null; }
}
