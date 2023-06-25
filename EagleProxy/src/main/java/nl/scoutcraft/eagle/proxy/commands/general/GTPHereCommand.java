package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

public class GTPHereCommand implements ICommand, IPlayerCommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("gtphere")
                .permission(Perms.GTPHERE)
                .usage("/gtphere <player>")
                .child(Args.player("target"), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        Player target = context.<Player>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        CommandUtils.globalTeleport(target, player);
    }
}
