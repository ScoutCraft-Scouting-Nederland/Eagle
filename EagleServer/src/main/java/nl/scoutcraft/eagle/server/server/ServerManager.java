package nl.scoutcraft.eagle.server.server;

import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.IServerChannel;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.network.ServerNetworkChannel;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ServerManager implements IServerChannel {

    private final NetworkChannel<IServerChannel> channel;
    private final Map<String, String> states;

    public ServerManager(EagleServer plugin) {
        this.channel = new ServerNetworkChannel<>("eagle:server", IServerChannel.class, this, plugin);
        this.states = Collections.synchronizedMap(new HashMap<>());
    }

    @Nullable
    public String getGameState(String serverName) {
        return this.states.get(serverName);
    }

    @Override
    public void setGameState(String serverName, String state) {
        if (state == null) {
            this.states.remove(serverName);
        } else {
            this.states.put(serverName, state);
        }
    }

    @Override
    public double[] getTPS() {
        return Bukkit.getTPS();
    }
}
