package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

public class PingCommand implements ICommand, ICommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("ping", "latency")
                .permission(Perms.PING)
                .executor(this)
                .child(ICommand.node(Args.player("target")).permission(Perms.PING_OTHER).executor(this::sendPingOther));
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws MessageException {
        if (sender instanceof Player player) {
            CommandMessages.PING_SELF.send(sender, new Placeholder("%ping%", Long.toString(player.getPing())));
            return;
        }

        CommandMessages.PING_NO_CONSOLE.send(sender);
    }

    private void sendPingOther(CommandSource sender, CommandContext context) throws MessageException {
        Player target = context.<Player>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        CommandMessages.PING_OTHER.send(sender, new Placeholder("%player%", target.getUsername()), new Placeholder("%ping%", String.valueOf(target.getPing())));
    }
}
