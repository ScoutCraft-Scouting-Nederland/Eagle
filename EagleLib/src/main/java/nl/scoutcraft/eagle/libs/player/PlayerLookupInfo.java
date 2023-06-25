package nl.scoutcraft.eagle.libs.player;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerLookupInfo {

    private final UUID id;
    private final String discordId;
    private final String name;
    private final String displayName;
    private final boolean online;
    private final String language;
    private final int version;
    private final String ip;
    private final String alts;
    private final LocalDateTime firstLogin;
    private final LocalDateTime lastLogin;
    private final LocalDateTime lastLogout;
    private final int playtime;

    public PlayerLookupInfo(UUID id, String discordId, String name, String displayName, boolean online, String language, int version, String ip, String alts, LocalDateTime firstLogin, LocalDateTime lastLogin, LocalDateTime lastLogout, int playtime) {
        this.id = id;
        this.discordId = discordId;
        this.name = name;
        this.displayName = displayName;
        this.online = online;
        this.language = language;
        this.version = version;
        this.ip = ip;
        this.alts = alts;
        this.firstLogin = firstLogin;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
        this.playtime = playtime;
    }

    public UUID getId() {
        return this.id;
    }

    public String getDiscordId() {
        return this.discordId;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean isOnline() {
        return this.online;
    }

    public String getLanguage() {
        return this.language;
    }

    public int getVersion() {
        return this.version;
    }

    public String getIp() {
        return this.ip;
    }

    public String getAlts() {
        return this.alts;
    }

    public LocalDateTime getFirstLogin() {
        return this.firstLogin;
    }

    public LocalDateTime getLastLogin() {
        return this.lastLogin;
    }

    public LocalDateTime getLastLogout() {
        return this.lastLogout;
    }

    public int getPlaytime() {
        return this.playtime;
    }
}
