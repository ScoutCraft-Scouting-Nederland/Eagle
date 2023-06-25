package nl.scoutcraft.eagle.proxy.commands.lib;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.Collections;
import java.util.List;

public interface ITabExecutor extends RawCommand {

    List<String> suggest(Player player, CommandContext context);

    @Override
    default List<String> suggest(Invocation invocation) {
        return invocation.source() instanceof Player ? this.suggest((Player) invocation.source(), new CommandContext(CommandNode.splitForTabCompletion(invocation.arguments()))) : Collections.emptyList();
    }
}
