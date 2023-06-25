package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;

import java.util.Collection;
import java.util.Collections;

public class SendCommand implements ICommand {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("send")
                .permission(Perms.SEND)
                .usage("/send {all|current|<server>|<player>} <target>")
                .child(ICommand.node("all", "global").usage("/send all <target>").child(Args.scoutServer("target"), (s, c) -> this.execute(s, c, EagleProxy.getProxy().getAllPlayers())))
                .child(ICommand.node("current", "local").usage("/send current <target>").child(Args.scoutServer("target"), (IPlayerCommandExecutor) (p, c) -> this.execute(p, c, p.getCurrentServer().map(s -> s.getServer().getPlayersConnected()).orElse(Collections.emptyList()))))
                .child(ICommand.node(Args.server("server")).usage("/send <server> <target>").child(Args.scoutServer("target"), (s, c) -> this.execute(s, c, c.<RegisteredServer>get("server").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", c.getRaw("server")))).getPlayersConnected())))
                .child(ICommand.node(Args.player("player")).usage("/send <player> <target>").child(Args.scoutServer("target"), (s, c) -> this.execute(s, c, Collections.singletonList(c.<Player>get("player").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", c.getRaw("player"))))))));
    }

    private void execute(CommandSource sender, CommandContext context, Collection<Player> players) throws MessageException {
        ScoutServer target = context.<ScoutServer>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("target"))));

        if (!target.getState())
            throw new MessageException(CommandMessages.SERVER_OFFLINE, new Placeholder("%server%", target.getDisplayNameOrName()));

        int connected = 0;
        for (Player player : players) {
            if (target.getServer().equals(player.getCurrentServer().map(ServerConnection::getServer).orElse(null))) continue;

            connected++;
            player.createConnectionRequest(target.getServer()).connectWithIndication().thenAcceptAsync(result -> {
                if (result)
                    CommandMessages.SEND_SUCCESS.send(player, new Placeholder("%server%", target.getDisplayNameOrName()), new Placeholder("%player%", sender instanceof Player p ? p.getUsername() : "Console"));
            });
        }

        CommandMessages.SEND_CONNECTED.send(sender, new Placeholder("%sent%", Integer.toString(connected)), new Placeholder("%tried%", Integer.toString(players.size())), new Placeholder("%server%", target.getDisplayNameOrName()));
    }
}
