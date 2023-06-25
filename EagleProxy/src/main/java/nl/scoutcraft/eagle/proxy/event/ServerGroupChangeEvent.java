package nl.scoutcraft.eagle.proxy.event;

import nl.scoutcraft.eagle.proxy.server.ScoutServer;

public record ServerGroupChangeEvent(ScoutServer server) {

    public ScoutServer getServer() {
        return this.server;
    }
}
