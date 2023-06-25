package nl.scoutcraft.eagle.proxy.commands.lib;

import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Argument;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandBuilder implements INodeBuilder<CommandBuilder> {

    private final String name;
    private final String[] aliases;

    @Nullable private String permission;
    @Nullable private String usage;
    @Nullable private ICommandExecutor executor;

    private final List<CommandNode> children;

    public CommandBuilder(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.children = new ArrayList<>();
    }

    @Override
    public CommandBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public CommandBuilder usage(String usage) {
        this.usage = usage;
        return this;
    }

    @Override
    public CommandBuilder executor(ICommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public CommandBuilder child(CommandNode node) {
        this.children.add(node);
        return this;
    }

    @Override
    public <T> CommandBuilder child(Node node) {
        return this.child(node.build());
    }

    @Override
    public <T> CommandBuilder child(Argument<T> arg, ICommandExecutor executor) {
        return this.child(new Node(arg).executor(executor).build());
    }

    @Override
    public CommandBuilder child(ICommandExecutor executor, String arg, String... aliases) {
        return this.child(new Node(Args.path(arg)).executor(executor).build());
    }

    @Override
    public CommandNode build() {
        if (this.name == null) throw new IllegalArgumentException("A command must have a name!");
        if (this.executor == null && this.usage == null) throw new IllegalArgumentException("");

        return new CommandNode(this.permission, this.usage, null, this.executor, this.children.toArray(CommandNode[]::new));
    }

    public String getName() {
        return this.name;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public static class Node implements INodeBuilder<CommandBuilder.Node> {

        private final Argument<?> arg;
        @Nullable private String permission;
        @Nullable private String usage;
        @Nullable private ICommandExecutor executor;

        private final List<CommandNode> children;

        Node(Argument<?> arg) {
            this.arg = arg;
            this.children = new ArrayList<>();
        }

        @Override
        public Node permission(String permission) {
            this.permission = permission;
            return this;
        }

        @Override
        public Node usage(String usage) {
            this.usage = usage;
            return this;
        }

        @Override
        public Node executor(ICommandExecutor executor) {
            this.executor = executor;
            return this;
        }

        @Override
        public Node child(CommandNode node) {
            this.children.add(node);
            return this;
        }

        @Override
        public <T> Node child(Node node) {
            return this.child(node.build());
        }

        @Override
        public <T> Node child(Argument<T> arg, ICommandExecutor executor) {
            return this.child(new Node(arg).executor(executor).build());
        }

        @Override
        public Node child(ICommandExecutor executor, String arg, String... aliases) {
            return this.child(new Node(Args.path(arg, aliases)).executor(executor).build());
        }

        @Override
        public CommandNode build() {
            if (this.executor == null && this.usage == null) throw new IllegalArgumentException("");

            return new CommandNode(this.permission, this.usage, this.arg, this.executor, this.children.toArray(CommandNode[]::new));
        }
    }
}
