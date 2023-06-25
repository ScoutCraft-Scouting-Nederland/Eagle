package nl.scoutcraft.eagle.libs.player;

import nl.scoutcraft.eagle.libs.sql.Identifiable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record PlayerInfo(UUID uuid, String name, @Nullable String displayName) implements Identifiable<UUID> {

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public UUID getIdentifier() {
        return this.uuid;
    }
}
