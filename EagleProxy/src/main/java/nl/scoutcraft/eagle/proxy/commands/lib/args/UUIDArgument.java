package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UUIDArgument extends Argument<UUID> {

    public UUIDArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public UUID transform(CommandSource sender, String arg, CommandContext context) {
        try {
            return UUID.fromString(arg);
        } catch (Exception exc) {
            return null;
        }
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return Collections.emptyList();
    }
}
