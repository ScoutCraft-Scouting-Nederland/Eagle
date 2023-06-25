package nl.scoutcraft.eagle.proxy.server;

public enum ServerGroup {
    LOBBY("lobby"),
    GAME("game");

    private final String name;

    ServerGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean matches(ScoutServer server) {
        return server.getGroups().contains(this.name);
    }
}
