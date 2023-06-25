package nl.scoutcraft.eagle.proxy.commands.lib.exc;

import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;

public class MessageException extends CommandException {

    private final IMessage message;
    private final IPlaceholder[] placeholders;

    public MessageException(IMessage message, IPlaceholder... placeholders) {
        this.message = message;
        this.placeholders = placeholders;
    }

    @Override
    public void send(CommandSource sender) {
        this.message.send(sender, this.placeholders);
    }
}
