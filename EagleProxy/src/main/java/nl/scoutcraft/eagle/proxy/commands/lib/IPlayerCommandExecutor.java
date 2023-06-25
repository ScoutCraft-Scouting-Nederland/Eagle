package nl.scoutcraft.eagle.proxy.commands.lib;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;

@FunctionalInterface
public interface IPlayerCommandExecutor extends ICommandExecutor {

    void execute(Player player, CommandContext context) throws CommandException;

    @Override
    default void execute(CommandSource sender, CommandContext context) throws CommandException {
        if (sender instanceof Player) this.execute((Player) sender, context);
        else CommandMessages.NO_CONSOLE.send(sender);
    }
}
