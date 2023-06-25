package nl.scoutcraft.eagle.proxy.commands.lib;

import com.velocitypowered.api.command.CommandManager;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Argument;

@FunctionalInterface
public interface ICommand {

    CommandBuilder build();

    default void register(CommandManager cm, EagleProxy plugin) {
        CommandBuilder command = this.build();
        cm.register(cm.metaBuilder(command.getName()).aliases(command.getAliases()).plugin(plugin).build(), command.build());
    }

    static CommandBuilder builder(String name, String... aliases) {
        return new CommandBuilder(name, aliases);
    }

    static <T> CommandBuilder.Node node(Argument<T> arg) {
        return new CommandBuilder.Node(arg);
    }

    static CommandBuilder.Node node(String arg, String... aliases) {
        return node(Args.path(arg, aliases));
    }

    static CommandBuilder.Node node(String arg, boolean silent, String... aliases) {
        return node(Args.path(arg, silent, aliases));
    }
}
