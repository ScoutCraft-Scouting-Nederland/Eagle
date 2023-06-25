package nl.scoutcraft.eagle.libs.party;

import java.util.UUID;

public class PartyInvitation {

    private final UUID uuid;
    private final String name;
    private final String textureProperty;

    public PartyInvitation(UUID uuid, String name, String textureProperty) {
        this.uuid = uuid;
        this.name = name;
        this.textureProperty = textureProperty;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getTextureProperty() {
        return this.textureProperty;
    }
}
