package nl.scoutcraft.eagle.proxy.commands.lib.exc;

import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

public class UsageException extends CommandException {

    private final String usage;

    public UsageException(String usage) {
        this.usage = usage;
    }

    @Override
    public void send(CommandSource sender) {
        CommandMessages.USAGE_FORMAT.send(sender, new Placeholder("%usage%", this.usage));
    }
}
