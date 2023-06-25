package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class PathArgument extends Argument<String> {

    private final String[] aliases;
    private final boolean silent;

    PathArgument(String name, boolean silent, String... aliases) {
        super(name);
        this.aliases = aliases;
        this.silent = silent;
    }

    @Override
    @Nullable
    public String transform(CommandSource sender, String arg, CommandContext context) {
        if (super.name.equalsIgnoreCase(arg))
            return super.name;

        for (String alias : this.aliases)
            if (alias.equalsIgnoreCase(arg))
                return alias;

        return null;
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return this.silent ? Collections.emptyList() : Collections.singletonList(super.name);
    }
}
