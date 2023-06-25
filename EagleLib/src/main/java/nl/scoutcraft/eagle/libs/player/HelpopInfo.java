package nl.scoutcraft.eagle.libs.player;

import nl.scoutcraft.eagle.libs.sql.Identifiable;

import java.time.LocalDateTime;
import java.util.UUID;

public class HelpopInfo implements Identifiable<UUID> {

    private final UUID id;
    private final UUID playerId;
    private final String playerName;
    private final UUID handledById;
    private final String handledByName;
    private final String message;
    private final LocalDateTime requestedAt;
    private final LocalDateTime acknowledgedAt;
    private final LocalDateTime resolvedAt;
    private final String status;
    private final int x;
    private final int y;
    private final int z;
    private final String world;
    private final String server;
    private final String discordMessageId;

    public HelpopInfo(UUID id, UUID playerId, String playerName, UUID handledById, String handledByName, String message, LocalDateTime requestedAt, LocalDateTime acknowledgedAt, LocalDateTime resolvedAt, String status, int x, int y, int z, String world, String server, String discordMessageId) {
        this.id = id;
        this.playerId = playerId;
        this.playerName = playerName;
        this.handledById = handledById;
        this.handledByName = handledByName;
        this.message = message;
        this.requestedAt = requestedAt;
        this.acknowledgedAt = acknowledgedAt;
        this.resolvedAt = resolvedAt;
        this.status = status;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.server = server;
        this.discordMessageId = discordMessageId;
    }

    public String getLocation() {
        return this.world == null ? "Unknown" : this.world + ", " + this.x + ", " + this.y + ", " + this.z;
    }

    @Override
    public UUID getIdentifier() {
        return this.id;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public UUID getHandledById() {
        return this.handledById;
    }

    public String getHandledByName() {
        return this.handledByName;
    }

    public String getMessage() {
        return this.message;
    }

    public LocalDateTime getRequestedAt() {
        return this.requestedAt;
    }

    public LocalDateTime getAcknowledgedAt() {
        return this.acknowledgedAt;
    }

    public LocalDateTime getResolvedAt() {
        return this.resolvedAt;
    }

    public String getStatus() {
        return this.status;
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

    public String getServer() {
        return this.server;
    }

    public String getDiscordMessageId() {
        return this.discordMessageId;
    }
}
