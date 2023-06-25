package nl.scoutcraft.eagle.proxy.server;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.event.ServerGroupChangeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoadBalancer {

    private final Map<String, ScoutServer> lobbies;

    public LoadBalancer() {
        this.lobbies = new HashMap<>();
    }

    public void load(ServerManager manager) {
        this.lobbies.clear();
        manager.getServersInGroup(ServerGroup.LOBBY).forEach(sc -> this.lobbies.put(sc.getName(), sc));
    }

    @Subscribe
    public void onServerChoose(PlayerChooseInitialServerEvent event) {
        this.getLeastActive().map(ScoutServer::getServer).ifPresent(event::setInitialServer);
    }

    @Subscribe
    public void onKick(KickedFromServerEvent event) {
        // TODO: Test if server shutdown trigger's this event.

        /*
        if (event.getCause() == ServerKickEvent.Cause.SERVER) {
            ServerInfo target = this.getLeastActive(event.getKickedFrom()).map(ScoutServer::getServerInfo).orElse(null);
            if (target != null && target != event.getKickedFrom()) {
                event.setCancelled(true);
                event.setCancelServer(target);
            }
        }
         */
    }

    @Subscribe(order = PostOrder.LAST)
    public void onConnect(ServerPreConnectEvent event) {

        // Als speler naar lobby gaat ->
        //     - speler komt uit lobby -> cancel if no bypass
        //     - speler komt niet uit lobby en geen bypass -> least active

        RegisteredServer target = event.getResult().getServer().orElse(event.getOriginalServer());

        if (this.isLobby(target)) {
            ServerConnection source = event.getPlayer().getCurrentServer().orElse(null);
            boolean isLobby = this.isLobby(source);

            if (isLobby && !event.getPlayer().hasPermission(Perms.LOADBALANCER_BYPASS))
                event.setResult(ServerPreConnectEvent.ServerResult.denied());

            if (!isLobby && !event.getPlayer().hasPermission(Perms.LOADBALANCER_BYPASS))
                this.getLeastActive().map(ScoutServer::getServer).ifPresent(lobby -> event.setResult(ServerPreConnectEvent.ServerResult.allowed(lobby)));
        }
    }

    @Subscribe
    public void onServerGroupChange(ServerGroupChangeEvent event) {
        ScoutServer server = event.getServer();

        if (ServerGroup.LOBBY.matches(server)) {
            this.lobbies.remove(server.getName());
        } else if (!this.lobbies.containsValue(server)) {
            this.lobbies.put(server.getName(), server);
        }
    }

    public void moveAll(RegisteredServer server) {
        server.getPlayersConnected().forEach(player -> this.getLeastActive(server).ifPresent(lobby -> player.createConnectionRequest(lobby.getServer()).fireAndForget()));
    }

    public Optional<ScoutServer> getLeastActive() {
        return this.lobbies.values().stream().filter(ScoutServer::getState).reduce((s1, s2) -> s1.getPlayers().size() <= s2.getPlayers().size() ? s1 : s2);
    }

    public Optional<ScoutServer> getLeastActive(RegisteredServer excluding) {
        return this.lobbies.values().stream().filter(ScoutServer::getState).filter(sc -> sc.getServer() != excluding).reduce((s1, s2) -> s1.getPlayers().size() <= s2.getPlayers().size() ? s1 : s2);
    }

    public boolean isLobby(@Nullable ServerConnection connection) {
        return connection != null && this.isLobby(connection.getServerInfo());
    }

    public boolean isLobby(@Nullable RegisteredServer server) {
        return server != null && this.isLobby(server.getServerInfo());
    }

    public boolean isLobby(@Nullable ServerInfo info) {
        return info != null && this.lobbies.containsKey(info.getName());
    }
}
