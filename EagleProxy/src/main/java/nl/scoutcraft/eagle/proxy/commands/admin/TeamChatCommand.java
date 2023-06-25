package nl.scoutcraft.eagle.proxy.commands.admin;

import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.chat.ChatChannels;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

public class TeamChatCommand implements ICommand, ICommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("teamchat").permission(Perms.CHAT_TEAM).executor(this);
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws CommandException {
        if (!context.hasRemaining())
            throw new MessageException(CommandMessages.GENERAL_ADD_MESSAGE);

        ChatChannels.TEAM.sendCommand(sender, TextUtils.toString(CommandUtils.buildMessage(context.getRemaining(), 0, sender.hasPermission(Perms.CHAT_COLORED))));
    }
}
