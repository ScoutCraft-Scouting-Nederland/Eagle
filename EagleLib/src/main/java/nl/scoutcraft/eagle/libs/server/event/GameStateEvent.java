package nl.scoutcraft.eagle.libs.server.event;

public class GameStateEvent {

    private final String server;
    private final String state;

    public GameStateEvent(String server, String state) {
        this.server = server;
        this.state = state;
    }

    public String getServer() {
        return this.server;
    }

    public String getState() {
        return this.state;
    }
}
