package nl.scoutcraft.eagle.proxy.commands.lib;

import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;

@FunctionalInterface
public interface ICommandExecutor {

    void execute(CommandSource sender, CommandContext context) throws CommandException;

    default boolean hasPermission(CommandSource sender) {
        return true;
    }
}
