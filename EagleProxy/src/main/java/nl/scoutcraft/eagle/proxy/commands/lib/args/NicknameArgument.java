package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class NicknameArgument extends Argument<Player> {

    public NicknameArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public Player transform(CommandSource sender, String arg, CommandContext context) {
        return CommandUtils.find(arg).orElse(null);
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return EagleProxy.getInstance().getPlayerManager().getScoutPlayers().stream().map(ScoutPlayer::getDisplayName).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }
}
