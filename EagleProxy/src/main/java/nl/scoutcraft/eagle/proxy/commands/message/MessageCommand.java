package nl.scoutcraft.eagle.proxy.commands.message;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

public class MessageCommand extends BaseMessageCommand implements ICommand, ICommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("message", "msg", "tell", "whisper")
                .permission(Perms.MSG)
                .usage("/msg <player> <message>")
                .child(Args.player("receiver"), this);
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws CommandException {
        if (!context.hasRemaining())
            throw new MessageException(CommandMessages.GENERAL_ADD_MESSAGE);

        Player receiver = context.<Player>get("receiver").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("receiver"))));

        super.sendMessage(sender, receiver, CommandUtils.buildMessage(context.getRemaining(), 0, sender.hasPermission(Perms.CHAT_COLORED)));
    }
}
