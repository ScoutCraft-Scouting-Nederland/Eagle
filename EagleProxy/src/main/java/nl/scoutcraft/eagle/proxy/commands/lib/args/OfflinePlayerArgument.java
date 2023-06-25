package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class OfflinePlayerArgument extends Argument<PlayerInfo> {

    public OfflinePlayerArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public PlayerInfo transform(CommandSource sender, String arg, CommandContext context) {
        Player player = CommandUtils.find(arg).orElse(null);
        if (player != null)
            return new PlayerInfo(player.getUniqueId(), player.getUsername(), EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player).getDisplayName().orElse(null));

        return EagleProxy.getInstance().getSQLManager().getPlayerInfo(arg);
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return EagleProxy.getInstance().getPlayerManager().getNames();
    }
}
