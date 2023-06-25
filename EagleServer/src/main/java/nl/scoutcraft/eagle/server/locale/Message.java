package nl.scoutcraft.eagle.server.locale;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public record Message(Internationalization lang, String key) implements IMessage {

    @Override
    public String getString(@Nullable Locale locale, IPlaceholder... placeholders) {
        return this.apply(this.lang.getString(locale, this.key), locale, placeholders);
    }

    @Override
    public Component get(@Nullable Locale locale, boolean colorized, IPlaceholder... placeholders) {
        return this.apply(colorized ? TextUtils.colorize(this.lang.getString(locale, this.key)) : Component.text(this.lang.getString(locale, this.key)), locale, placeholders);
    }
}
