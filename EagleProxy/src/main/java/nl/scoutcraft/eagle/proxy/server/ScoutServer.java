package nl.scoutcraft.eagle.proxy.server;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.libs.server.GameState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ScoutServer {

    private final UUID id;
    private final Set<String> groups;

    private String name;
    private String ip;
    private int port;

    private RegisteredServer server;
    private boolean state;

    @Nullable
    private String displayName;
    @Nullable
    private GameState gameState;

    ScoutServer(UUID id, String name, String ip, int port, RegisteredServer server, @Nullable String displayName) {
        this.id = id;
        this.groups = new HashSet<>();
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.server = server;
        this.state = false;
        this.displayName = displayName;
    }

    public String getDisplayNameOrName() {
        return this.displayName != null ? this.displayName : this.name;
    }

    public Collection<Player> getPlayers() {
        return this.server.getPlayersConnected();
    }

    public UUID getId() {
        return this.id;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    protected void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    protected void setPort(int port) {
        this.port = port;
    }

    public RegisteredServer getServer() {
        return this.server;
    }

    protected void setServer(RegisteredServer server) {
        this.server = server;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Nullable
    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    @Nullable
    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(@Nullable GameState gameState) {
        this.gameState = gameState;
    }
}
