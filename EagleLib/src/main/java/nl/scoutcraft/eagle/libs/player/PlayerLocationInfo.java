package nl.scoutcraft.eagle.libs.player;

import java.util.UUID;

public class PlayerLocationInfo {

    private final UUID uuid;
    private final int x, y, z;
    private final String world;

    public PlayerLocationInfo(UUID uuid, int x, int y, int z, String world) {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public String getWorld() {
        return this.world;
    }
}
