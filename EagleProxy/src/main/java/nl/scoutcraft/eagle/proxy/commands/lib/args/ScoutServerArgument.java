package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

public class ScoutServerArgument extends Argument<ScoutServer> {

    public ScoutServerArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public ScoutServer transform(CommandSource sender, String arg, CommandContext context) {
        return EagleProxy.getInstance().getServerManager().getServer(arg);
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return EagleProxy.getProxy().getAllServers().stream().map(s -> s.getServerInfo().getName()).collect(Collectors.toList());
    }
}
