package nl.scoutcraft.eagle.proxy.commands.lib;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Argument;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandNode implements RawCommand, ICommandExecutor, ITabExecutor {

    @Nullable private final String permission;
    @Nullable private final String usage;
    @Nullable private final Argument<?> arg;
    @Nullable private final ICommandExecutor executor;

    private final CommandNode[] children;

    public CommandNode(@Nullable String permission, @Nullable String usage, @Nullable Argument<?> arg, @Nullable ICommandExecutor executor, CommandNode[] children) {
        this.permission = permission;
        this.usage = usage;
        this.arg = arg;
        this.executor = executor;
        this.children = children;
    }

    @Override
    public void execute(Invocation invocation) {
        try {
            this.execute(invocation.source(), new CommandContext(splitForExecution(invocation.arguments())));
        } catch (CommandException exc) {
            exc.send(invocation.source());
        }
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws CommandException {
        if (this.permission != null && !sender.hasPermission(this.permission))
            throw new MessageException(CommandMessages.NO_PERMISSION);

        // Solo Child Handoff
        if (context.hasRemaining() && this.children.length == 1) {
            if (this.children[0].arg != null && context.hasRemaining())
                this.children[0].arg.addToContext(sender, context.removeNext(), context);
            this.children[0].execute(sender, context);
            return;
        }

        // Child parsing
        for (CommandNode node : this.children) {
            if (node.transform(sender, context)) {
                node.execute(sender, context);
                return;
            }
        }

        // No child found
        if (this.executor != null) this.executor.execute(sender, context);
        else if (this.usage != null) CommandMessages.USAGE_FORMAT.send(sender, new Placeholder("%usage%", this.usage));
    }

    @Override
    public List<String> suggest(Player player, CommandContext context) {
        if (!context.hasRemaining() || !this.hasPermission(player))
            return Collections.emptyList();

        // Argument parsing
        if (this.arg != null) {
            String arg = context.removeNext();

            if (!context.hasRemaining())
                return CommandUtils.copyPartialMatches(arg, this.arg.getSuggestions(player), new ArrayList<>());
        }

        // Child parsing
        if (context.getRemainingSize() > 1)
            for (CommandNode child : this.children)
                if (child.matches(player, context))
                    return child.suggest(player, context);

        // Child suggestions
        if (context.getRemainingSize() == 1) {
            List<String> options = new ArrayList<>();

            for (CommandNode child : this.children)
                options.addAll(child.suggest(player, context.copyRaw()));

            return options;
        }

        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return this.hasPermission(invocation.source());
    }

    @Override
    public boolean hasPermission(CommandSource sender) {
        return this.permission == null || sender.hasPermission(this.permission);
    }

    private boolean matches(CommandSource sender, CommandContext context) {
        if (this.arg == null)
            return true;

        if (context.hasRemaining())
            return this.arg.matches(sender, context.copyRaw(), context.getNext());

        return !this.arg.isRequired();
    }

    public boolean transform(CommandSource sender, CommandContext context) {
        if (this.arg == null)
            return true;

        if (context.hasRemaining()) {
            if (this.arg.addToContextIfValid(sender, context.getNext(), context)) {
                context.removeNext();

                return true;
            } else return false;
        }

        return !this.arg.isRequired();
    }

    static String[] splitForTabCompletion(String argsString) {
        String[] split = argsString.trim().split(" ");
        if (!argsString.endsWith(" "))
            return split;

        String[] args = new String[split.length + 1];
        args[split.length] = "";
        System.arraycopy(split, 0, args, 0, split.length);
        return args;
    }

    static String[] splitForExecution(String argsString) {
        if (argsString.isEmpty())
            return new String[0];

        return argsString.trim().split(" ");
    }
}
