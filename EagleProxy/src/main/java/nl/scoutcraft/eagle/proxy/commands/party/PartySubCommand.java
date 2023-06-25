package nl.scoutcraft.eagle.proxy.commands.party;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;

public abstract class PartySubCommand {

    private final IMessage message;
    private final String usage;
    private final String permission;
    private final String[] commands;

    public PartySubCommand(IMessage message, String usage, String permission, String... commands) {
        this.message = message;
        this.usage = usage;
        this.permission = permission;
        this.commands = commands;
    }

    public abstract void onCommand(Player player, String[] args);

    public boolean matches(String command) {
        for (String cmd : this.commands)
            if (cmd.equalsIgnoreCase(command))
                return true;

        return false;
    }

    public String getCommandName() {
        return this.commands[0];
    }

    public IMessage getMessage() {
        return this.message;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getPermission() {
        return this.permission;
    }

    public String[] getCommands() {
        return this.commands;
    }
}
