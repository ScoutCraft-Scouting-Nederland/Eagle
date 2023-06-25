package nl.scoutcraft.eagle.proxy.commands.admin;

import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;

public class EagleCommand implements ICommand {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("eagle")
                .permission(Perms.RELOAD)
                .usage("/eagle {reload}")
                .child(this::reload, "reload");
    }

    private void reload(CommandSource sender, CommandContext context) {
        EagleProxy.getInstance().reload();
        CommandMessages.EAGLE_RELOADED.send(sender);
    }
}
