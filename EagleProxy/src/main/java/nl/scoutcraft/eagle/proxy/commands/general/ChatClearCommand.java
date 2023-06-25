package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

public class ChatClearCommand implements ICommand, ICommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("chatclear", "clearchat")
                .permission(Perms.CHAT_CLEAR)
                .usage("/chatclear {all|current|<server>}")
                .child(ICommand.node("all", "global").usage("/chatclear all").executor((s, c) -> this.execute(s, EagleProxy.getProxy().getAllServers())))
                .child(ICommand.node("current", "local").usage("/chatclear current").executor((IPlayerCommandExecutor) (p, c) -> this.execute(p, p.getCurrentServer().map(s -> Collections.singletonList(s.getServer())).orElse(Collections.emptyList()))))
                .child(ICommand.node(Args.server("target")).usage("/chatclear <server>").executor(this));
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws MessageException {
        this.execute(sender, Collections.singletonList(context.<RegisteredServer>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("target"))))));
    }

    private void execute(CommandSource sender, Collection<RegisteredServer> servers) {
        String senderName = sender instanceof Player ? ((Player) sender).getUsername() : "Console";

        servers.stream()
                .flatMap(server -> server.getPlayersConnected().stream())
                .filter(player -> !player.hasPermission(Perms.CHAT_CLEAR_BYPASS))
                .forEach(player -> {
                    IntStream.range(0, 100).mapToObj(i -> Component.empty()).forEach(player::sendMessage);
                    if (!player.equals(sender))
                        CommandMessages.CLEARCHAT_MESSAGE.send(player, new Placeholder("%clearer%", senderName));
                });

        CommandMessages.CLEARCHAT_MESSAGE_CLEARER.send(sender, new Placeholder("%server%", servers.size() > 1 ? "global" : servers.iterator().next().getServerInfo().getName()));
    }
}
