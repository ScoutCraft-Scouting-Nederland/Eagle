package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class ChoiceArgument<T> extends Argument<T> {

    private final Map<String, T> choices;

    ChoiceArgument(String name, Map<String, T> choices) {
        super(name);
        this.choices = choices;
    }

    public void add(String key, T value) {
        this.choices.put(key, value);
    }

    @Override
    @Nullable
    public T transform(CommandSource sender, String arg, CommandContext context) {
        return this.choices.get(arg.toLowerCase());
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return this.choices.keySet();
    }
}
