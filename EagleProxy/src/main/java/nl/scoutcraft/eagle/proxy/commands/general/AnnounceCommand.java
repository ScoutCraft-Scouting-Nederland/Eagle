package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.UsageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.ComponentPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

import java.util.Collection;
import java.util.Collections;

public class AnnounceCommand implements ICommand {

    public CommandBuilder build() {
        return ICommand.builder("announce", "broadcast")
                .permission(Perms.ANNOUNCE)
                .usage("/announce {chat|title|subtitle} {all|current|<server>} <message>")
                .child(ICommand.node("chat")
                        .permission(Perms.ANNOUNCE_CHAT)
                        .usage("/announce chat {all|current|<server>} <message>")
                        .child(ICommand.node("all", "global").executor((s, c) -> this.sendChatAnnouncement(s, c, EagleProxy.getProxy().getAllServers())))
                        .child(ICommand.node("current", "local").executor((IPlayerCommandExecutor) (p, c) -> this.sendChatAnnouncement(p, c, p.getCurrentServer().map(s -> Collections.singletonList(s.getServer())).orElse(Collections.emptyList()))))
                        .child(ICommand.node(Args.server("target")).executor((s, c) -> this.sendChatAnnouncement(s, c, Collections.singletonList(c.<RegisteredServer>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", c.getRaw("target")))))))))
                .child(ICommand.node("title")
                        .permission(Perms.ANNOUNCE_TITLE)
                        .usage("/announce title {all|current|<server>} <message>")
                        .child(ICommand.node("all", "global").executor((s, c) -> this.sendTitleAnnouncement(s, c, EagleProxy.getProxy().getAllServers())))
                        .child(ICommand.node("current", "local").executor((IPlayerCommandExecutor) (p, c) -> this.sendTitleAnnouncement(p, c, p.getCurrentServer().map(s -> Collections.singletonList(s.getServer())).orElse(Collections.emptyList()))))
                        .child(ICommand.node(Args.server("target")).executor((s, c) -> this.sendTitleAnnouncement(s, c, Collections.singletonList(c.<RegisteredServer>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", c.getRaw("target")))))))))
                .child(ICommand.node("subtitle")
                        .permission(Perms.ANNOUNCE_SUBTITLE)
                        .usage("/announce subtitle {all|current|<server>} <message>")
                        .child(ICommand.node("all", "global").executor((s, c) -> this.sendSubtitleAnnouncement(s, c, EagleProxy.getProxy().getAllServers())))
                        .child(ICommand.node("current", "local").executor((IPlayerCommandExecutor) (p, c) -> this.sendSubtitleAnnouncement(p, c, p.getCurrentServer().map(s -> Collections.singletonList(s.getServer())).orElse(Collections.emptyList()))))
                        .child(ICommand.node(Args.server("target")).executor((s, c) -> this.sendSubtitleAnnouncement(s, c, Collections.singletonList(c.<RegisteredServer>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", c.getRaw("target")))))))));
    }

    private void sendChatAnnouncement(CommandSource sender, CommandContext context, Collection<RegisteredServer> servers) throws UsageException {
        if (!context.hasRemaining()) throw new UsageException("/announce chat " + context.getRaw("target"));

        IPlaceholder placeholder = new ComponentPlaceholder("%message%", CommandUtils.buildMessage(context.getRemaining(), 0, true));
        servers.forEach(s -> s.getPlayersConnected().forEach(p -> CommandMessages.ANNOUNCE_CHAT_FORMAT.send(p, placeholder)));
        CommandMessages.ANNOUNCE_SENT.send(sender, new Placeholder("%target%", servers.size() > 1 ? "global" : servers.iterator().next().getServerInfo().getName()));
    }

    private void sendTitleAnnouncement(CommandSource sender, CommandContext context, Collection<RegisteredServer> servers) throws UsageException {
        if (!context.hasRemaining()) throw new UsageException("/announce title " + context.getRaw("target"));

        Title title = Title.title(CommandUtils.buildMessage(context.getRemaining(), 0, true), Component.empty(), Title.Times.of(Ticks.duration(10), Ticks.duration(100), Ticks.duration(10)));
        servers.forEach(s -> s.getPlayersConnected().forEach(p -> p.showTitle(title)));
        CommandMessages.ANNOUNCE_SENT.send(sender, new Placeholder("%target%", servers.size() > 1 ? "global" : servers.iterator().next().getServerInfo().getName()));
    }

    private void sendSubtitleAnnouncement(CommandSource sender, CommandContext context, Collection<RegisteredServer> servers) throws UsageException {
        if (!context.hasRemaining()) throw new UsageException("/announce subtitle " + context.getRaw("target"));

        Title title = Title.title(Component.empty(), CommandUtils.buildMessage(context.getRemaining(), 0, true), Title.Times.of(Ticks.duration(10), Ticks.duration(100), Ticks.duration(10)));
        servers.forEach(s -> s.getPlayersConnected().forEach(p -> p.showTitle(title)));
        CommandMessages.ANNOUNCE_SENT.send(sender, new Placeholder("%target%", servers.size() > 1 ? "global" : servers.iterator().next().getServerInfo().getName()));
    }
}
