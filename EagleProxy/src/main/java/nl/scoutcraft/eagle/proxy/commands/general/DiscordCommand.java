package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;

public class DiscordCommand implements ICommand, IPlayerCommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("discord").executor(this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        CommandMessages.DISCORD_INFO.send(player);
    }
}
