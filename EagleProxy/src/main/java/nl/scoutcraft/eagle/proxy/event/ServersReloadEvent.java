package nl.scoutcraft.eagle.proxy.event;

import nl.scoutcraft.eagle.proxy.server.ServerManager;

public record ServersReloadEvent(ServerManager serverManager) {

    public ServerManager getServerManager() {
        return serverManager;
    }
}
