package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;

public class ServerCommand implements ICommand, IPlayerCommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("server")
                .permission(Perms.SERVER)
                .usage("/server <server>")
                .child(Args.scoutServer("target"), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws MessageException {
        ScoutServer target = context.<ScoutServer>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("target"))));

        if (!target.getState())
            throw new MessageException(CommandMessages.SERVER_OFFLINE, new Placeholder("%server%", target.getDisplayNameOrName()));

        if (target.getServer().equals(player.getCurrentServer().orElse(null)))
            throw new MessageException(CommandMessages.SERVER_ALREADY_CONNECTED, new Placeholder("%server%", target.getDisplayNameOrName()));

        player.createConnectionRequest(target.getServer()).fireAndForget();
        CommandMessages.SERVER_CONNECTED.send(player, new Placeholder("%server%", target.getDisplayNameOrName()));
    }
}
