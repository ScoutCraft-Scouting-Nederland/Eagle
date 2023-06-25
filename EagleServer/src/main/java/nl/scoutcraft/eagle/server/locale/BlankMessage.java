package nl.scoutcraft.eagle.server.locale;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class BlankMessage implements IMessage {

    @Override
    public String getString(@Nullable CommandSender target, IPlaceholder... placeholders) {
        return "";
    }

    @Override
    public String getString(@Nullable Locale locale, IPlaceholder... placeholders) {
        return "";
    }

    @Override
    public Component get(@Nullable CommandSender target, IPlaceholder... placeholders) {
        return Component.empty();
    }

    @Override
    public Component get(@Nullable CommandSender target, boolean colorized, IPlaceholder... placeholders) {
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
    public void send(CommandSender target, IPlaceholder... placeholders) {
    }

    @Override
    public void send(CommandSender target, @Nullable Locale locale, IPlaceholder... placeholders) {
    }

    @Override
    public void sendActionBar(Player player, IPlaceholder... placeholders) {
    }

    @Override
    public void sendActionBar(Player player, @Nullable Locale locale, IPlaceholder... placeholders) {
    }

    @Override
    public void sendTitle(Player player, @Nullable IMessage subtitle, int fadeIn, int stay, int fadeOut, IPlaceholder... placeholders) {
    }

    @Override
    public void sendTitle(Player player, @Nullable Locale locale, @Nullable IMessage subtitle, int fadeIn, int stay, int fadeOut, IPlaceholder... placeholders) {
    }
}
