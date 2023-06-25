package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;

import java.util.Locale;

public class LangCommand implements ICommand, IPlayerCommandExecutor {

    private static final EagleProxy PLUGIN = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("lang", "locale", "language")
                .permission(Perms.LANG)
                .usage("/lang <language>")
                .child(Args.locale("lang"), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        Locale locale = context.<Locale>get("lang").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_LANG));

        if (!PLUGIN.getLangManager().getCommandLang().getLocales().contains(locale))
            throw new MessageException(CommandMessages.GENERAL_INVALID_LANG);

        PLUGIN.getPlayerManager().setLocale(player, locale);
        CommandMessages.LANG_SET.send(player);
    }
}
