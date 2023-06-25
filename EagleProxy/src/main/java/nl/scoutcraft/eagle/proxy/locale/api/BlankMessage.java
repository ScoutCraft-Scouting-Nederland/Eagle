package nl.scoutcraft.eagle.proxy.locale.api;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class BlankMessage implements IMessage {

    @Override
    public String getString() {
        return "";
    }

    @Override
    public String getString(@Nullable Locale locale, IPlaceholder... placeholders) {
        return "";
    }

    @Override
    public Component get(@Nullable CommandSource target, IPlaceholder... placeholders) {
        return Component.empty();
    }

    @Override
    public Component get(@Nullable CommandSource target, boolean colorized, IPlaceholder... placeholders) {
        return Component.empty();
    }

    @Override
    public Component get(@Nullable Locale locale, IPlaceholder... placeholders) {
        return Component.empty();
    }

    @Override
    public Component get(@Nullable Locale locale, boolean colorized, IPlaceholder... placeholders) {
        return Component.empty();
    }

    @Override
    public void send(CommandSource target, IPlaceholder... placeholders) {
    }

    @Override
    public void sendActionBar(Player player, IPlaceholder... placeholders) {
    }

    @Override
    public void send(CommandSource target, @Nullable Locale locale, IPlaceholder... placeholders) {
    }

    @Override
    public void sendActionBar(Player player, @Nullable Locale locale, IPlaceholder... placeholders) {
    }
}
