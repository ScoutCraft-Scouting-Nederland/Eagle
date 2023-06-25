package nl.scoutcraft.eagle.proxy.commands.lib.exc;

import com.velocitypowered.api.command.CommandSource;

public abstract class CommandException extends Exception {

    public abstract void send(CommandSource sender);
}
