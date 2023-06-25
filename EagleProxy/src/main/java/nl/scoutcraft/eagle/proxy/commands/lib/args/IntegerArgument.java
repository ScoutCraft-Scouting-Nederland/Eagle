package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class IntegerArgument extends Argument<Integer> {

    public IntegerArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public Integer transform(CommandSource sender, String arg, CommandContext context) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException exc) {
            return null;
        }
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return Collections.emptyList();
    }
}
