package nl.scoutcraft.eagle.libs.player;

import nl.scoutcraft.eagle.libs.sql.Identifiable;

import java.time.LocalDateTime;
import java.util.UUID;

public class BugInfo implements Identifiable<UUID> {

    private final UUID id;
    private final UUID reporterId;
    private final String reporterName;
    private final UUID handledById;
    private final String handledByName;
    private final String message;
    private final String resolvedMessage;
    private final LocalDateTime reportedAt;
    private final LocalDateTime acknowledgedAt;
    private final LocalDateTime resolvedAt;
    private final String serverName;
    private final String discordMessageId;

    public BugInfo(UUID uuid, UUID reporterId, String reporterName, UUID handledById, String handledByName, String message, String resolvedMessage, LocalDateTime reportedAt, LocalDateTime acknowledgedAt, LocalDateTime resolvedAt, String serverName, String discordMessageId) {
        this.id = uuid;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.handledById = handledById;
        this.handledByName = handledByName;
        this.message = message;
        this.resolvedMessage = resolvedMessage;
        this.reportedAt = reportedAt;
        this.acknowledgedAt = acknowledgedAt;
        this.resolvedAt = resolvedAt;
        this.serverName = serverName;
        this.discordMessageId = discordMessageId;
    }

    @Override
    public UUID getIdentifier() {
        return this.id;
    }

    public UUID getReporterId() {
        return this.reporterId;
    }

    public String getReporterName() {
        return this.reporterName;
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

    public String getResolvedMessage() {
        return this.resolvedMessage;
    }

    public LocalDateTime getReportedAt() {
        return this.reportedAt;
    }

    public LocalDateTime getAcknowledgedAt() {
        return this.acknowledgedAt;
    }

    public LocalDateTime getResolvedAt() {
        return this.resolvedAt;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getDiscordMessageId() {
        return this.discordMessageId;
    }
}
