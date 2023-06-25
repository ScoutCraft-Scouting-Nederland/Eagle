package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

public class LocaleArgument extends Argument<Locale> {

    public LocaleArgument(String name) {
        super(name);
    }

    @Nullable
    @Override
    public Locale transform(CommandSource sender, String arg, CommandContext context) {
        return Locale.forLanguageTag(arg);
    }

    @Override
    public Collection<String> getSuggestions(Player player) {
        return EagleProxy.getInstance().getLangManager().getCommandLang().getLocales().stream().map(Locale::toLanguageTag).collect(Collectors.toList());
    }
}
