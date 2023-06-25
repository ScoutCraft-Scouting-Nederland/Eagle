package nl.scoutcraft.eagle.libs.server.event;

import java.util.UUID;

public class ServerStateEvent {

    private final UUID id;
    private final boolean state;

    public ServerStateEvent(UUID id, boolean state) {
        this.id = id;
        this.state = state;
    }

    public UUID getId() {
        return this.id;
    }

    public boolean isState() {
        return this.state;
    }
}
