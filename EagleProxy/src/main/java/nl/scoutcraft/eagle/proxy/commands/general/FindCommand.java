package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

public class FindCommand implements ICommand, ICommandExecutor {

    @Override
    public CommandBuilder build() {
        return ICommand.builder("find")
                .permission(Perms.FIND)
                .usage("/find <player>")
                .child(Args.player("target"), this);
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws MessageException {
        Player target = context.<Player>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        CommandMessages.FIND_FOUND.send(sender, new Placeholder("%player%", target.getUsername()), new Placeholder("%server%", target.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Unknown")));
    }
}
