package nl.scoutcraft.eagle.proxy.commands.general;

import com.google.common.collect.Lists;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.LuckPermsProvider;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.ComponentPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;
import nl.scoutcraft.eagle.proxy.server.ServerManager;

import java.util.Collection;
import java.util.List;

public class GListCommand implements ICommand, ICommandExecutor {

    private final ServerManager serverManager = EagleProxy.getInstance().getServerManager();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("glist").permission(Perms.GLIST).executor(this);
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws MessageException {
        Collection<ScoutServer> servers = this.serverManager.getServers(); // ProxyServer.getInstance().getServersCopy().values();
        servers.forEach(server -> {
            IMessage status = server.getState() ? CommandMessages.ONLINE : CommandMessages.OFFLINE;
            sender.sendMessage(CommandMessages.GLIST_SERVER.get(sender,
                            new Placeholder("%server%", server.getServer().getServerInfo().getName()),
                            new Placeholder("%online%", Integer.toString(server.getServer().getPlayersConnected().size())),
                            new ComponentPlaceholder("%players%", this.getPlayersComponent(sender, server)))
                    .hoverEvent(HoverEvent.showText(CommandMessages.GLIST_HOVER_STATUS.get(sender, new MessagePlaceholder("%status%", status))))
                    .clickEvent(ClickEvent.runCommand("/server " + server.getServer().getServerInfo().getName())));
        });

        CommandMessages.GLIST_TOTAL_ONLINE.send(sender, new Placeholder("%online%", Integer.toString(EagleProxy.getProxy().getAllPlayers().size())));
    }

    private Component getPlayersComponent(CommandSource sender, ScoutServer server) {
        List<Player> players = Lists.newArrayList(server.getPlayers());
        if (players.isEmpty())
            return Component.empty();

        Component comp = this.getPlayerComponent(sender, players.get(0));
        for (int i = 1; i < players.size(); i++)
            comp = comp.append(Component.text(", ")).append(this.getPlayerComponent(sender, players.get(i)));

        return comp;
    }

    private Component getPlayerComponent(CommandSource sender, Player player) {
        return Component.text(player.getUsername(), this.getRankColor(player))
                .hoverEvent(HoverEvent.showText(CommandMessages.GLIST_HOVER_DISPLAYNAME.get(sender, new Placeholder("%nickname%", player.getUsername()), new Placeholder("%uuid%", player.getUniqueId().toString()))))
                .clickEvent(ClickEvent.copyToClipboard(player.getUniqueId().toString()));
    }

    private TextColor getRankColor(Player player) {
        String prefix = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getCachedData().getMetaData().getPrefix();
        if (prefix == null)
            return Colors.GRAY;

        Component text = TextUtils.colorize(prefix);

        return text.color() != null ? text.color() : Colors.GRAY;
    }
}
