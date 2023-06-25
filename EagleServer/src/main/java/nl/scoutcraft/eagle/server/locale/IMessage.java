package nl.scoutcraft.eagle.server.locale;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface IMessage {

    default String getString(@Nullable CommandSender target, IPlaceholder... placeholders) {
        return this.getString(this.getLocale(target), placeholders);
    }

    String getString(@Nullable Locale locale, IPlaceholder... placeholders);

    default Component get(@Nullable CommandSender target, IPlaceholder... placeholders) {
        return this.get(target, true, placeholders);
    }

    default Component get(@Nullable CommandSender target, boolean colorized, IPlaceholder... placeholders) {
        return this.get(this.getLocale(target), colorized, placeholders);
    }

    default Component get(@Nullable Locale locale, IPlaceholder... placeholders) {
        return this.get(locale, true, placeholders);
    }

    default Component get(@Nullable Locale locale, boolean colorized, IPlaceholder... placeholders) {
        return colorized ? TextUtils.colorize(this.getString(locale, placeholders)) : Component.text(this.getString(locale, placeholders));
    }

    default void send(CommandSender target, IPlaceholder... placeholders) {
        this.send(target, this.getLocale(target), placeholders);
    }

    default void send(CommandSender target, @Nullable Locale locale, IPlaceholder... placeholders) {
        target.sendMessage(this.get(locale, placeholders));
    }

    default void sendActionBar(Player player, IPlaceholder... placeholders) {
        this.sendActionBar(player, this.getLocale(player), placeholders);
    }

    default void sendActionBar(Player player, @Nullable Locale locale, IPlaceholder... placeholders) {
        player.sendActionBar(this.get(locale, true, placeholders));
    }

    default void sendTitle(Player player, @Nullable IMessage subtitle, int fadeIn, int stay, int fadeOut, IPlaceholder... placeholders) {
        this.sendTitle(player, this.getLocale(player), subtitle, fadeIn, stay, fadeOut, placeholders);
    }

    default void sendTitle(Player player, @Nullable Locale locale, @Nullable IMessage subtitle, int fadeIn, int stay, int fadeOut, IPlaceholder... placeholders) {
        Component mainTitle = this.get(locale, true, placeholders);
        Component sub = (subtitle != null ? subtitle.get(locale, true, placeholders) : Component.empty());
        player.showTitle(Title.title(mainTitle, sub, Title.Times.of(Ticks.duration(fadeIn), Ticks.duration(stay), Ticks.duration(fadeOut))));
    }

    @Nullable
    default Locale getLocale(@Nullable CommandSender target) {
        return target instanceof Player player ? player.getPersistentDataContainer().get(EagleKeys.LOCALE, EagleKeys.LOCALE_TAG_TYPE) : null;
    }

    default String apply(String str, @Nullable Locale locale, IPlaceholder... placeholders) {
        for (IPlaceholder placeholder : placeholders)
            str = placeholder.apply(str, locale);
        return str;
    }

    default Component apply(Component comp, @Nullable Locale locale, IPlaceholder... placeholders) {
        for (IPlaceholder placeholder : placeholders)
            comp = placeholder.apply(comp, locale);
        return comp;
    }
}
