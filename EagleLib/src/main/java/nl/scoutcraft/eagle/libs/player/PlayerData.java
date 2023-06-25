package nl.scoutcraft.eagle.libs.player;

import nl.scoutcraft.eagle.libs.sql.Identifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.UUID;

public class PlayerData implements Identifiable<UUID> {

    private final UUID playerId;
    @Nullable private final String prefix;
    @Nullable private final String displayName;
    @Nullable private final Locale locale;

    public PlayerData(UUID playerId, @Nullable String prefix, @Nullable String displayName, @Nullable Locale locale) {
        this.playerId = playerId;
        this.prefix = prefix;
        this.displayName = displayName;
        this.locale = locale;
    }

    @Override
    public UUID getIdentifier() {
        return this.playerId;
    }

    @Nullable
    public String getPrefix() {
        return this.prefix;
    }

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    @Nullable
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "playerId=" + playerId +
                ", prefix='" + prefix + '\'' +
                ", displayName='" + displayName + '\'' +
                ", locale=" + locale +
                '}';
    }
}
