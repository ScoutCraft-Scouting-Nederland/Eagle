package nl.scoutcraft.eagle.proxy.commands.lib;

import nl.scoutcraft.eagle.proxy.commands.lib.args.Argument;

public interface INodeBuilder<B extends INodeBuilder<? super B>> {

    B permission(String permission);
    B usage(String usage);
    B executor(ICommandExecutor executor);

    B child(CommandNode node);
    <T> B child(CommandBuilder.Node node);
    <T> B child(Argument<T> arg, ICommandExecutor executor);
    B child(ICommandExecutor executor, String arg, String... aliases);

    CommandNode build();
}
