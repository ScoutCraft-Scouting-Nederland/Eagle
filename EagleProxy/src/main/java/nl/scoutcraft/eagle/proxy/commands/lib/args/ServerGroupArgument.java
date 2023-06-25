package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ServerGroupArgument extends Argument<String> {

    public ServerGroupArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public String transform(CommandSource sender, String arg, CommandContext context) {
        return EagleProxy.getInstance().getServerManager().getGroups().contains(arg) ? arg : null;
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return EagleProxy.getInstance().getServerManager().getGroups();
    }
}
