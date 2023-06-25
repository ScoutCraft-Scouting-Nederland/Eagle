package nl.scoutcraft.eagle.proxy.commands.message;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

import java.util.UUID;

public class ReplyCommand extends BaseMessageCommand implements ICommand, ICommandExecutor {

    private static final String USAGE = "/r <message>";

    @Override
    public CommandBuilder build() {
        return ICommand.builder("reply", "r")
                .permission(Perms.REPLY)
                .usage(USAGE)
                .executor(this);
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws CommandException {
        if (!context.hasRemaining())
            throw new MessageException(CommandMessages.GENERAL_ADD_MESSAGE);

        UUID receiverUuid = sender instanceof Player player ? EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player).getReplyTarget().orElseThrow(() -> new MessageException(CommandMessages.REPLY_NO_TARGET)) : CONSOLE_REPLY_TARGET;

        CommandSource receiver = receiverUuid.equals(CONSOLE_UUID) ? EagleProxy.getProxy().getConsoleCommandSource() : EagleProxy.getProxy().getPlayer(receiverUuid).orElseThrow(() -> new MessageException(CommandMessages.REPLY_NO_TARGET));

        super.sendMessage(sender, receiver, CommandUtils.buildMessage(context.getRemaining(), 0, sender.hasPermission(Perms.CHAT_COLORED)));
    }
}
