package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class StringArgument extends Argument<String> {

    public final List<String> defaults;

    StringArgument(String name, List<String> defaults) {
        super(name);

        this.defaults = defaults;
    }

    @Override
    @Nullable
    public String transform(CommandSource sender, String arg, CommandContext context) {
        return arg.isEmpty() ? null : arg;
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return this.defaults;
    }
}
