package nl.scoutcraft.eagle.libs.party;

import java.util.UUID;

public class PartyMemberInfo {

    private final UUID uuid;
    private final String name;
    private final String texture;

    public PartyMemberInfo(UUID uuid, String name, String texture) {
        this.uuid = uuid;
        this.name = name;
        this.texture = texture;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getTexture() {
        return this.texture;
    }
}
