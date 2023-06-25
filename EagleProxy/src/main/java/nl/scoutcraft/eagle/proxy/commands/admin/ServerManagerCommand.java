package nl.scoutcraft.eagle.proxy.commands.admin;

import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.UsageException;
import nl.scoutcraft.eagle.proxy.io.DatabaseManager;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;
import nl.scoutcraft.eagle.proxy.server.ServerManager;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

public class ServerManagerCommand implements ICommand {

    private final ServerManager serverManager = EagleProxy.getInstance().getServerManager();
    private final DatabaseManager sqlManager = EagleProxy.getInstance().getSQLManager();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("servermanager")
                .permission(Perms.SERVERMANAGER)
                .usage("/servermanager {servers|groups|reload}")
//                .executor("servers")
                .child(ICommand.node("reload").permission(Perms.SERVERMANAGER_RELOAD).executor(this::reload))
                .child(ICommand.node("servers")
                        .permission(Perms.SERVERMANAGER_SERVERS)
                        .usage("/servermanager servers {list|add|remove|<server>}")
//                        .executor("list")
                        .child(ICommand.node(Args.path("list").optional()).permission(Perms.SERVERMANAGER_SERVERS_LIST).executor(this::sendServerList))
                        .child(ICommand.node("add").permission(Perms.SERVERMANAGER_SERVERS_EDIT).usage("/servermanager servers add <name> <ip> <port> {displayname}")
                                .child(ICommand.node(Args.string("name")).usage("/servermanager servers add <name> <ip> <port> {displayname}")
                                        .child(ICommand.node(Args.string("ip")).usage("/servermanager servers add <name> <ip> <port> {displayname}")
                                                .child(ICommand.node(Args.port("port")).executor(this::addServer)))))
                        .child(ICommand.node("remove", "delete").permission(Perms.SERVERMANAGER_SERVERS_EDIT).usage("/servermanager servers remove <name>").child(Args.scoutServer("server"), this::removeServer))
                        .child(ICommand.node(Args.scoutServer("server"))
                                .usage("/servermanager servers <server> {info|ip|port|name|displayname}")
//                                .executor("info")
                                .child(ICommand.node(Args.path("info").optional()).permission(Perms.SERVERMANAGER_SERVERS_SERVER_INFO).executor(this::sendServerInfo))
                                .child(ICommand.node("name").permission(Perms.SERVERMANAGER_SERVERS_SERVER_EDIT).usage("/servermanager servers <server> name <name>").child(Args.string("value"), this::setServerName))
                                .child(ICommand.node("displayname").permission(Perms.SERVERMANAGER_SERVERS_SERVER_EDIT).executor(this::setServerDisplayName))
                                .child(ICommand.node("ip").permission(Perms.SERVERMANAGER_SERVERS_SERVER_EDIT).usage("/servermanager servers <server> ip <ip>").child(Args.string("value"), this::setServerIp))
                                .child(ICommand.node("port").permission(Perms.SERVERMANAGER_SERVERS_SERVER_EDIT).usage("/servermanager servers <server> port <port>").child(Args.integer("value"), this::setServerPort))))
                .child(ICommand.node("groups")
                        .permission(Perms.SERVERMANAGER_GROUPS)
                        .usage("/servermanager groups {list|create|delete|<group>}")
//                        .executor("list")
                        .child(ICommand.node(Args.path("list").optional()).permission(Perms.SERVERMANAGER_GROUPS_LIST).executor(this::sendGroupList))
                        .child(ICommand.node("add").permission(Perms.SERVERMANAGER_GROUPS_EDIT).usage("/servermanager groups add <name>").child(Args.string("group"), this::addGroup))
                        .child(ICommand.node("remove", "delete").permission(Perms.SERVERMANAGER_GROUPS_EDIT).usage("/servermanager groups remove <name>").child(Args.serverGroup("group"), this::deleteGroup))
                        .child(ICommand.node(Args.serverGroup("group"))
                                .usage("/servermanager groups <group> {info|add|remove}")
//                                .executor("info")
                                .child(ICommand.node(Args.path("info").optional()).permission(Perms.SERVERMANAGER_GROUPS_GROUP_INFO).executor(this::sendGroupInfo))
                                .child(ICommand.node("add").permission(Perms.SERVERMANAGER_GROUPS_GROUP_EDIT).usage("/servermanager groups <group> add <server>").child(Args.scoutServer("server"), this::addServerToGroup))
                                .child(ICommand.node("remove", "delete").permission(Perms.SERVERMANAGER_GROUPS_GROUP_EDIT).usage("/servermanager groups <group> remove <server>").child(Args.scoutServer("server"), this::removeServerFromGroup))));
    }

    private void reload(CommandSource sender, CommandContext context) {
        this.serverManager.load();
        CommandMessages.SERVERMANAGER_RELOADED.send(sender);
    }

    // SERVERS

    private void sendServerList(CommandSource sender, CommandContext context) {
        sender.sendMessage(Component.empty());
        sender.sendMessage(TextUtils.text(TextUtils.line(29), Component.text(" Server List ", Colors.GOLD, TextDecoration.BOLD), TextUtils.line(29)));
        this.serverManager.getServers().forEach(server -> {
            IMessage format = server.getDisplayName() == null ? CommandMessages.SERVERMANAGER_SERVER_LIST_FORMAT : CommandMessages.SERVERMANAGER_SERVER_LIST_FORMAT_DISPLAYNAME;
            sender.sendMessage(format.get(sender, new Placeholder("%name%", server.getName()), new Placeholder("%displayname%", server.getDisplayName() == null ? "" : server.getDisplayName()), new Placeholder("%ip%", server.getIp()), new Placeholder("%port%", Integer.toString(server.getPort())))
                    .hoverEvent(HoverEvent.showText(Component.text("Click for more info.", Colors.GRAY)))
                    .clickEvent(ClickEvent.runCommand("/servermanager servers " + server.getName() + " info")));
        });
        sender.sendMessage(TextUtils.line(79));
    }

    private void addServer(CommandSource sender, CommandContext context) throws CommandException {
        String name = context.<String>get("name").get().toLowerCase();
        String ip = context.<String>get("ip").get();
        int port = context.<Integer>get("port").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PORT, new Placeholder("%port%", context.getRaw("port"))));
        String displayName = context.hasRemaining() ? TextUtils.toString(CommandUtils.buildMessage(context.getRemaining(), 0, false)) : null;

        if (this.serverManager.getServer(name) != null)
            throw new MessageException(CommandMessages.SERVERMANAGER_SERVER_ALREADY_EXISTS, new Placeholder("%server%", name));

        this.serverManager.addServer(name, ip, port, displayName);
        CommandMessages.SERVERMANAGER_SERVER_ADDED.send(sender, new Placeholder("%server%", name));
    }

    private void removeServer(CommandSource sender, CommandContext context) throws CommandException {
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));

        this.serverManager.removeServer(server.getName());
        CommandMessages.SERVERMANAGER_SERVER_REMOVED.send(sender, new Placeholder("%server%", server.getName()));
    }

    private void sendServerInfo(CommandSource sender, CommandContext context) throws CommandException {
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));
        IMessage format = CommandMessages.SERVERMANAGER_SERVER_INFO_FORMAT;

        sender.sendMessage(Component.text(' '));
        sender.sendMessage(TextUtils.text(TextUtils.line(29), Component.text(" Server Info ", Colors.GOLD, TextDecoration.BOLD), TextUtils.line(29)));
        format.send(sender, new Placeholder("%key%", "Name"), new Placeholder("%value%", server.getName()));
        format.send(sender, new Placeholder("%key%", "DisplayName"), new Placeholder("%value%", server.getDisplayName() != null ? server.getDisplayName() : "None"));
        format.send(sender, new Placeholder("%key%", "IP"), new Placeholder("%value%", server.getIp()));
        format.send(sender, new Placeholder("%key%", "Port"), new Placeholder("%value%", Integer.toString(server.getPort())));
        format.send(sender, new Placeholder("%key%", "Groups"), new Placeholder("%value%", TextUtils.concat(", ", server.getGroups().toArray(new String[0]))));
        format.send(sender, new Placeholder("%key%", "State"), new MessagePlaceholder("%value%", (server.getState() ? CommandMessages.ONLINE : CommandMessages.OFFLINE)));
        format.send(sender, new Placeholder("%key%", "GameState"), new Placeholder("%value%", (server.getGameState() != null ? server.getGameState().getId() : "None")));
        format.send(sender, new Placeholder("%key%", "Players online"), new Placeholder("%value%", Integer.toString(server.getPlayers().size())));
        sender.sendMessage(TextUtils.line(79));
    }

    private void setServerName(CommandSource sender, CommandContext context) throws CommandException {
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));
        String serverName = context.<String>get("value").get().toLowerCase();

        this.serverManager.editServer(server, serverName, server.getIp(), server.getPort());
        this.sqlManager.updateServer(server);
        CommandMessages.SERVERMANAGER_SERVER_EDITED_NAME.send(sender, new Placeholder("%name%", serverName));
    }

    private void setServerDisplayName(CommandSource sender, CommandContext context) throws CommandException {
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));
        if (!context.hasRemaining())
            throw new UsageException("/servermanager servers <server> displayname <name...>");

        Component name = CommandUtils.buildMessage(context.getRemaining(), 0, false);
        server.setDisplayName(TextUtils.toString(name));
        this.sqlManager.updateServer(server);
        CommandMessages.SERVERMANAGER_SERVER_EDITED_DISPLAYNAME.send(sender, new Placeholder("%name%", server.getDisplayName()));
    }

    private void setServerIp(CommandSource sender, CommandContext context) throws CommandException {
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));
        String ip = context.<String>get("value").get();

        this.serverManager.editServer(server, server.getName(), ip, server.getPort());
        this.sqlManager.updateServer(server);
        CommandMessages.SERVERMANAGER_SERVER_EDITED_IP.send(sender, new Placeholder("%ip%", ip));
    }

    private void setServerPort(CommandSource sender, CommandContext context) throws CommandException {
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));
        int port = context.<Integer>get("value").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PORT, new Placeholder("%port%", context.getRaw("value"))));

        this.serverManager.editServer(server, server.getName(), server.getIp(), port);
        this.sqlManager.updateServer(server);
        CommandMessages.SERVERMANAGER_SERVER_EDITED_PORT.send(sender, new Placeholder("%port%", Integer.toString(port)));
    }

    // GROUPS

    private void sendGroupList(CommandSource sender, CommandContext context) {
        sender.sendMessage(Component.empty());
        sender.sendMessage(TextUtils.text(TextUtils.line(30), Component.text(" Group List ", Colors.GOLD, TextDecoration.BOLD), TextUtils.line(30)));
        this.serverManager.getGroups().forEach(group -> sender.sendMessage(CommandMessages.SERVERMANAGER_GROUP_LIST_FORMAT.get(sender, new Placeholder("%name%", group))
                .hoverEvent(HoverEvent.showText(Component.text("Click for more info.", Colors.GRAY)))
                .clickEvent(ClickEvent.runCommand("/servermanager groups " + group + " info"))));
        sender.sendMessage(TextUtils.line(79));
    }

    private void addGroup(CommandSource sender, CommandContext context) throws CommandException {
        String group = context.<String>get("group").get();

        if (this.serverManager.getGroups().contains(group))
            throw new MessageException(CommandMessages.SERVERMANAGER_GROUP_ALREADY_EXISTS, new Placeholder("%group%", group));

        this.serverManager.addGroup(group);
        CommandMessages.SERVERMANAGER_GROUP_ADDED.send(sender, new Placeholder("%group%", group));
    }

    private void deleteGroup(CommandSource sender, CommandContext context) throws CommandException {
        String group = context.<String>get("group").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_GROUP, new Placeholder("%group%", context.getRaw("group"))));

        this.serverManager.getServers().forEach(sc -> sc.getGroups().remove(group));
        this.serverManager.deleteGroup(group);
        CommandMessages.SERVERMANAGER_GROUP_REMOVED.send(sender, new Placeholder("%group%", group));
    }

    private void sendGroupInfo(CommandSource sender, CommandContext context) throws CommandException {
        String group = context.<String>get("group").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_GROUP, new Placeholder("%group%", context.getRaw("group"))));
        IMessage format = CommandMessages.SERVERMANAGER_SERVER_INFO_FORMAT;

        sender.sendMessage(Component.text(' '));
        sender.sendMessage(TextUtils.text(TextUtils.line(30), Component.text(" Group Info ", Colors.GOLD, TextDecoration.BOLD), TextUtils.line(30)));
        format.send(sender, new Placeholder("%key%", "Name"), new Placeholder("%value%", group));
        format.send(sender, new Placeholder("%key%", "Servers"), new Placeholder("%value%", TextUtils.concat(", ", this.serverManager.getServers().stream().filter(sc -> sc.getGroups().contains(group)).map(ScoutServer::getDisplayNameOrName).toArray(String[]::new))));
        sender.sendMessage(TextUtils.line(79));
    }

    private void addServerToGroup(CommandSource sender, CommandContext context) throws CommandException {
        String group = context.<String>get("group").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_GROUP, new Placeholder("%group%", context.getRaw("group"))));
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));

        if (server.getGroups().contains(group))
            throw new MessageException(CommandMessages.SERVERMANAGER_SERVER_ALREADY_IN_GROUP, new Placeholder("%group%", group), new Placeholder("%server%", server.getDisplayNameOrName()));

        this.serverManager.addGroupToServer(group, server);
        CommandMessages.SERVERMANAGER_SERVER_ADDED_TO_GROUP.send(sender, new Placeholder("%group%", group), new Placeholder("%server%", server.getDisplayNameOrName()));
    }

    private void removeServerFromGroup(CommandSource sender, CommandContext context) throws CommandException {
        String group = context.<String>get("group").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_GROUP, new Placeholder("%group%", context.getRaw("group"))));
        ScoutServer server = context.<ScoutServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("server"))));

        if (!server.getGroups().contains(group))
            throw new MessageException(CommandMessages.SERVERMANAGER_SERVER_NOT_IN_GROUP, new Placeholder("%group%", group), new Placeholder("%server%", server.getDisplayNameOrName()));

        this.serverManager.removeGroupFromServer(group, server);
        CommandMessages.SERVERMANAGER_SERVER_REMOVED_FROM_GROUP.send(sender, new Placeholder("%group%", group), new Placeholder("%server%", server.getDisplayNameOrName()));
    }

/*
    /servermanager reload

    /servermanager servers list
    /servermanager servers add {servername} {ip} {port}
    /servermanager servers remove {servername}
    /servermanager servers {servername} info
    /servermanager servers {servername} set name {name}
    /servermanager servers {servername} set ip {ip}
    /servermanager servers {servername} set port {port}

    /servermanager groups list
    /servermanager groups add {groupname}
    /servermanager groups delete {groupname}
    /servermanager groups {groupname} info
    /servermanager groups {groupname} add {servername}
    /servermanager groups {groupname} remove {servername}
*/
}
