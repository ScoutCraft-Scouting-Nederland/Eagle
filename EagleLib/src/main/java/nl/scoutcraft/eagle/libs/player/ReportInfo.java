package nl.scoutcraft.eagle.libs.player;

import nl.scoutcraft.eagle.libs.sql.Identifiable;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReportInfo implements Identifiable<UUID> {

    private final UUID id;
    private final UUID reporterId;
    private final String reporterName;
    private final UUID reporteeId;
    private final String reporteeName;
    private final UUID handledById;
    private final String handledByName;
    private final String message;
    private final LocalDateTime reportedAt;
    private final String status;
    private final LocalDateTime acknowledgedAt;
    private final LocalDateTime resolvedAt;
    private final String serverName;
    private final String discordMessageId;

    public ReportInfo(UUID uuid, UUID reporterId, String reporterName, UUID reporteeId, String reporteeName, UUID handledById, String handledByName, String message, LocalDateTime reportedAt, String status, LocalDateTime acknowledgedAt, LocalDateTime resolvedAt, String serverName, String discordMessageId) {
        this.id = uuid;
        this.reporterId = reporterId;
        this.reporterName = reporterName;
        this.reporteeId = reporteeId;
        this.reporteeName = reporteeName;
        this.handledById = handledById;
        this.handledByName = handledByName;
        this.message = message;
        this.reportedAt = reportedAt;
        this.status = status;
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

    public UUID getReporteeId() {
        return this.reporteeId;
    }

    public String getReporteeName() {
        return this.reporteeName;
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

    public LocalDateTime getReportedAt() {
        return this.reportedAt;
    }

    public String getStatus() {
        return this.status;
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
