package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class IgnoreeArgument extends Argument<PlayerInfo> {

    public IgnoreeArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public PlayerInfo transform(CommandSource sender, String arg, CommandContext context) {
        return EagleProxy.getInstance().getChatManager().getIgnores().getPlayerInfo(arg);
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return CommandUtils.getIgnorees(player.getUniqueId());
    }
}
