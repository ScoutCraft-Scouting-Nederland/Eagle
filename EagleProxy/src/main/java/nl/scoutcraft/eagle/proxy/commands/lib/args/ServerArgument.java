package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

public class ServerArgument extends Argument<RegisteredServer> {

    public ServerArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public RegisteredServer transform(CommandSource sender, String arg, CommandContext context) {
        return EagleProxy.getProxy().getServer(arg).orElse(null);
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return EagleProxy.getProxy().getAllServers().stream().map(s -> s.getServerInfo().getName()).collect(Collectors.toList());
    }
}
