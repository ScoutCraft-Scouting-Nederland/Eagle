package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class Argument<T> {

    protected final String name;
    protected boolean required;

    public Argument(String name) {
        this.name = name;
        this.required = true;
    }

    public Argument<T> optional() {
        this.required = false;
        return this;
    }

    public boolean matches(CommandSource sender, CommandContext context, String arg) {
        return this.transform(sender, arg, context) != null;
    }

    public boolean addToContextIfValid(CommandSource sender, String arg, CommandContext context) {
        T parsed = this.transform(sender, arg, context);
        if (parsed == null)
            return false;

        context.add(this, arg, parsed);
        return true;
    }

    public void addToContext(CommandSource sender, String arg, CommandContext context) {
        context.add(this, arg, this.transform(sender, arg, context));
    }

    public String getName() {
        return this.name;
    }

    public boolean isRequired() {
        return this.required;
    }

    @Nullable
    public abstract T transform(CommandSource sender, String arg, CommandContext context);

    public abstract Collection<String> getSuggestions(Player player);
}
