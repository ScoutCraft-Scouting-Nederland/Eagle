package nl.scoutcraft.eagle.proxy.locale.api;

import com.mojang.brigadier.Message;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Locale;

public interface IMessage extends Message {

    @Override
    default String getString() {
        return this.getString((Locale) null);
    }

    default String getString(@Nullable CommandSource target, IPlaceholder... placeholders) {
        return this.getString(this.getLocale(target), placeholders);
    }

    String getString(@Nullable Locale locale, IPlaceholder... placeholders);

    default Component get(@Nullable CommandSource target, IPlaceholder... placeholders) {
        return this.get(target, true, placeholders);
    }

    default Component get(@Nullable CommandSource target, boolean colorized, IPlaceholder... placeholders) {
        return this.get(this.getLocale(target), colorized, placeholders);
    }

    default Component get(@Nullable Locale locale, IPlaceholder... placeholders) {
        return this.get(locale, true, placeholders);
    }

    Component get(@Nullable Locale locale, boolean colorized, IPlaceholder... placeholders);

/*    {
        return colorized ? TextUtils.colorize(this.getString(locale, placeholders)) : Component.text(this.getString(locale, placeholders));
    }*/

    default void send(CommandSource target, IPlaceholder... placeholders) {
        this.send(target, this.getLocale(target), placeholders);
    }

    default void send(CommandSource target, @Nullable Locale locale, IPlaceholder... placeholders) {
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
        Title.Times times = Title.Times.of(Duration.ofSeconds(fadeIn), Duration.ofSeconds(stay), Duration.ofSeconds(fadeOut));
        player.showTitle(Title.title(mainTitle, sub, times));
    }

    @Nullable
    default Locale getLocale(@Nullable CommandSource target) {
        return target instanceof Player ? EagleProxy.getInstance().getPlayerManager().getScoutPlayer((Player) target).getLocale().orElse(null) : null;
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
