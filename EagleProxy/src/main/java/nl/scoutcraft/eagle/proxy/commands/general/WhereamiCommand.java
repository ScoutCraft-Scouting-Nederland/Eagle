package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;

public class WhereamiCommand implements ICommand, IPlayerCommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("whereami").permission(Perms.WHEREAMI).executor(this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        ScoutServer server = player.getCurrentServer().map(s -> EagleProxy.getInstance().getServerManager().getServer(s.getServer())).orElseThrow(() -> new MessageException(CommandMessages.WHEREAMI_FAILED));
        CommandMessages.WHEREAMI_MSG.send(player, new Placeholder("%server%", server.getDisplayNameOrName()));
    }
}
