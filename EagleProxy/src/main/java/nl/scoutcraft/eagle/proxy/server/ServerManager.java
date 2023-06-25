package nl.scoutcraft.eagle.proxy.server;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.IServerChannel;
import nl.scoutcraft.eagle.libs.server.GameState;
import nl.scoutcraft.eagle.libs.server.ServerInfo;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.discord.DiscordChannel;
import nl.scoutcraft.eagle.proxy.discord.DiscordEmbeds;
import nl.scoutcraft.eagle.proxy.discord.IDiscordManager;
import nl.scoutcraft.eagle.proxy.event.ConfigReloadEvent;
import nl.scoutcraft.eagle.proxy.event.ServerGroupChangeEvent;
import nl.scoutcraft.eagle.proxy.event.ServersReloadEvent;
import nl.scoutcraft.eagle.proxy.io.ProxyNetworkChannel;
import nl.scoutcraft.eagle.proxy.locale.GeneralMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.server.tasks.HeartbeatCheckTask;
import nl.scoutcraft.eagle.proxy.server.tasks.TPSTask;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerManager implements IServerChannel {

    private final EagleProxy plugin;
    private final LoadBalancer loadBalancer;
    private final HeartbeatCheckTask heartbeatTask;
    private final TPSTask tpsTask;

    private final Collection<String> groups;
    private final Map<String, ScoutServer> servers;

    private final NetworkChannel<IServerChannel> channel;

    public ServerManager(EagleProxy plugin) {
        this.plugin = plugin;
        this.loadBalancer = new LoadBalancer();
        this.heartbeatTask = new HeartbeatCheckTask(plugin);
        this.tpsTask = new TPSTask(plugin);

        this.groups = new HashSet<>();
        this.servers = new HashMap<>();

        this.channel = new ProxyNetworkChannel<>("eagle:server", IServerChannel.class, this, plugin);
    }

    public void load() {
        this.plugin.getLogger().info("Loading servers...");

        this.heartbeatTask.stop();
        this.tpsTask.stop();

        this.groups.clear();
        this.groups.addAll(this.plugin.getSQLManager().getAllServerGroups());

        ProxyServer proxy = EagleProxy.getProxy();

        //Map<String, RegisteredServer> servers = EagleProxy.getProxy().getAllServers().stream().collect(Collectors.toMap(rs -> rs.getServerInfo().getName(), rs -> rs));
        Map<String, ServerInfo> db = this.plugin.getSQLManager().getAllServers();
        Map<UUID, List<String>> serversInGroups = this.plugin.getSQLManager().getServersInGroups();

        // Load servers from database
        db.values().forEach(serverData -> {
            String name = serverData.getName();
            RegisteredServer server = proxy.getServer(name).orElseGet(() -> proxy.registerServer(new com.velocitypowered.api.proxy.server.ServerInfo(name, new InetSocketAddress(serverData.getIp(), serverData.getPort()))));
            ScoutServer sc = this.servers.computeIfAbsent(name.toLowerCase(), k -> new ScoutServer(serverData.getId(), name, serverData.getIp(), serverData.getPort(), server, serverData.getDisplayName()));
            sc.getGroups().clear();
            Optional.ofNullable(serversInGroups.get(serverData.getId())).ifPresent(sc.getGroups()::addAll);
            this.plugin.getLogger().info("Loaded server: " + serverData.getName());
        });

        // Loops through all servers to remove servers that are not present in the database
        Set<String> removed = this.servers.values().stream().map(ScoutServer::getName).filter(name -> !db.containsKey(name)).collect(Collectors.toSet());

        EagleProxy.getProxy().getAllServers().stream().filter(server -> !db.containsKey(server.getServerInfo().getName())).map(server -> server.getServerInfo().getName()).forEach(removed::add);

        removed.forEach(name -> {
            this.servers.remove(name.toLowerCase());
            proxy.getServer(name).ifPresent(server -> {
                this.loadBalancer.moveAll(server);
                proxy.unregisterServer(server.getServerInfo());
            });
            this.plugin.getLogger().warn("Server '" + name + "' no longer exists in the database, and was therefore removed!");
        });

        this.loadBalancer.load(this);
        proxy.getEventManager().fireAndForget(new ServersReloadEvent(this));

        this.heartbeatTask.start();
        this.tpsTask.start();

        this.plugin.getLogger().info("Loaded servers successfully!");
    }

    @Subscribe
    public void onConfigReload(ConfigReloadEvent event) {
        this.heartbeatTask.load(event.getConfig());
        this.tpsTask.load(event.getConfig());
    }

    /* ############
     * on start of server get all servers from db, servername / ip / port, and add them to the getServers()
     *
     * When adding server, do check if it exists in db, if not, add it to db.
     * Then automatically add it to the servers list -> ProxyServer.getInstance().getServers().put(serverName, serverInfo);
     *
     * When removing server, do check if it exists in db, if not send message of no existence.
     * If it does exist, remove it from db and from serverslist -> ProxyServer.getInstance().getServers().remove(serverName, serverInfo);
     *
     * when adding or removing server on webpanel, send popup that you need to do something like (/servermanager reload), then do check if serverslist is equal to db servers,
     *   if not add or remove servers that are or are not in db.
     *
     * Make it only possible to remove servers that are stated as OFFLINE.
     * If server is not offline send message that you need to shutdown server first.
     *
     * Make it possible to create server "groups". Like for instance the lobbies.
     * Create a group where you can put server id's in, and if player wants to connect to {name of lobby} send them to one of the available lobbies in that group.
     *
     * list of all servers (/servermanager servers)
     * list of all groups (/servermanager groups)
     * */

    public void addServer(String serverName, String ip, int port, @Nullable String displayName) {
        if (this.servers.containsKey(serverName)) return;

        UUID id = UUID.randomUUID();
        RegisteredServer registeredServer = EagleProxy.getProxy().registerServer(new com.velocitypowered.api.proxy.server.ServerInfo(serverName, new InetSocketAddress(ip, port)));

        this.servers.put(serverName.toLowerCase(), new ScoutServer(id, serverName, ip, port, registeredServer, displayName));
        this.plugin.getSQLManager().addServer(id, serverName, ip, port, displayName);

        this.plugin.getLogger().info("Loaded server: " + serverName);
    }

    public void removeServer(String serverName) {
        Optional<RegisteredServer> server = EagleProxy.getProxy().getServer(serverName);
        if (this.servers.remove(serverName.toLowerCase()) == null || server.isEmpty()) return;

        EagleProxy.getProxy().unregisterServer(new com.velocitypowered.api.proxy.server.ServerInfo(serverName, server.get().getServerInfo().getAddress()));
        this.plugin.getSQLManager().deleteServer(serverName);

        server.ifPresent(this.loadBalancer::moveAll);

        this.plugin.getLogger().info("Unloaded server: " + serverName);
    }

    public void editServer(ScoutServer server, String serverName, String ip, int port) {
        this.loadBalancer.moveAll(server.getServer());

        Optional<RegisteredServer> editable = EagleProxy.getProxy().getServer(serverName);
        if (editable.isEmpty()) return;

        EagleProxy.getProxy().unregisterServer(new com.velocitypowered.api.proxy.server.ServerInfo(serverName, editable.get().getServerInfo().getAddress()));
        RegisteredServer registeredServer = EagleProxy.getProxy().registerServer(new com.velocitypowered.api.proxy.server.ServerInfo(serverName, new InetSocketAddress(ip, port)));

        this.servers.remove(server.getName().toLowerCase());
        this.servers.put(serverName.toLowerCase(), server);

        server.setName(serverName);
        server.setIp(ip);
        server.setPort(port);
        server.setServer(registeredServer);
    }

    public void addGroup(String group) {
        if (this.groups.contains(group)) return;

        this.groups.add(group);
        this.plugin.getSQLManager().addGroup(group);
    }

    public void deleteGroup(String group) {
        if (!this.groups.contains(group)) return;

        this.groups.remove(group);
        this.servers.values().forEach(sc -> sc.getGroups().remove(group));
        this.plugin.getSQLManager().removeAllServersFromGroup(group);
        this.plugin.getSQLManager().deleteGroup(group);
    }

    public void addGroupToServer(String group, ScoutServer server) {
        if (server.getGroups().contains(group)) return;

        server.getGroups().add(group);
        EagleProxy.getProxy().getEventManager().fireAndForget(new ServerGroupChangeEvent(server));
        this.plugin.getSQLManager().addServerToGroup(server.getId(), group);
    }

    public void removeGroupFromServer(String group, ScoutServer server) {
        if (!server.getGroups().contains(group)) return;

        server.getGroups().remove(group);
        EagleProxy.getProxy().getEventManager().fireAndForget(new ServerGroupChangeEvent(server));
        this.plugin.getSQLManager().removeServerFromGroup(server.getId(), group);
    }

    public void setState(ScoutServer server, boolean state) {
        if (server.getState() == state) return;
        server.setState(state);

        InetSocketAddress address = server.getServer().getServerInfo().getAddress();
        this.plugin.getSQLManager().setServerState(server.getId(), state);

        Runnable discordAction = () -> this.plugin.getDiscordManager().sendChannelMessage(DiscordChannel.MONITORING, DiscordEmbeds.serverState(server.getName(), address.getHostName(), address.getPort(), state));

        IDiscordManager discord = this.plugin.getDiscordManager();
        if (discord.isReady()) discordAction.run();
        else discord.addReadyAction(discordAction);

        if (ServerGroup.GAME.matches(server)) {
            if (!state) {
                server.setGameState(GameState.OFFLINE);
            } else if (server.getGameState() == GameState.OFFLINE) {
                server.setGameState(GameState.WAITING);
            }
        }

        EagleProxy.getProxy().getAllPlayers().stream()
                .filter(p -> p.hasPermission(Perms.SERVER_STATUS))
                .forEach(p -> GeneralMessages.SERVER_PING.send(p, new Placeholder("%server%", server.getName()), new MessagePlaceholder("%status%", state ? GeneralMessages.ONLINE : GeneralMessages.OFFLINE)));
    }

    public NetworkChannel<IServerChannel> getChannel() {
        return this.channel;
    }

    public LoadBalancer getLoadBalancer() {
        return this.loadBalancer;
    }

    public Collection<String> getGroups() {
        return this.groups;
    }

    public Collection<ScoutServer> getServers() {
        return this.servers.values();
    }

    public Stream<ScoutServer> getServersInGroup(ServerGroup group) {
        return this.servers.values().stream().filter(group::matches);
    }

    public ScoutServer getServer(RegisteredServer server) {
        return getServer(server.getServerInfo().getName());
    }

    public ScoutServer getServer(com.velocitypowered.api.proxy.server.ServerInfo serverInfo) {
        return getServer(serverInfo.getName());
    }

    @Nullable
    public ScoutServer getServer(String name) {
        return this.servers.get(name.toLowerCase());
    }

    @Override
    public void setGameState(String state) {
        Object conn = this.channel.getConnection();
        RegisteredServer server = conn instanceof Player ? ((Player) conn).getCurrentServer().map(ServerConnection::getServer).orElse(null) : conn instanceof ServerConnection ? ((ServerConnection) conn).getServer() : null;
        if (server == null)
            return;

        this.getServer(server).setGameState(GameState.of(state));

        List<ScoutServer> servers = this.getServersInGroup(ServerGroup.GAME).collect(Collectors.toList());
        this.getServersInGroup(ServerGroup.LOBBY).filter(lobby -> !lobby.getPlayers().isEmpty()).forEach(lobby -> servers.forEach(gameServer -> {
            if (gameServer.getGameState() != null)
                this.channel.request().setTarget(lobby).setGameState(gameServer.getName(), this.getMessage(gameServer.getGameState()).getString((Locale) null));
        }));
    }

    private IMessage getMessage(GameState state) {
        return switch (state) {
            case WAITING -> GeneralMessages.GAMESTATE_WAITING;
            case FULL -> GeneralMessages.GAMESTATE_FULL;
            case IN_GAME -> GeneralMessages.GAMESTATE_INGAME;
            case RESTARTING -> GeneralMessages.GAMESTATE_RESTARTING;
            default -> GeneralMessages.OFFLINE;
        };
    }
}
