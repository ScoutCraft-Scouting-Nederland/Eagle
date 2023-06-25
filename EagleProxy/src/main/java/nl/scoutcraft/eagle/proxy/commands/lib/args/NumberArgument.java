package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class NumberArgument extends Argument<Double> {

    public NumberArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public Double transform(CommandSource sender, String arg, CommandContext context) {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException exc) {
            return null;
        }
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return Collections.emptyList();
    }
}
