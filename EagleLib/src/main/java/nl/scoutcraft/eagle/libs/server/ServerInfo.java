package nl.scoutcraft.eagle.libs.server;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ServerInfo {

    private final UUID id;
    private final String name;
    private final String ip;
    private final int port;
    private final boolean online;
    @Nullable private final String displayName;

    public ServerInfo(UUID id, String name, String ip, int port, boolean online, @Nullable String displayName) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.online = online;
        this.displayName = displayName;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isOnline() {
        return this.online;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
