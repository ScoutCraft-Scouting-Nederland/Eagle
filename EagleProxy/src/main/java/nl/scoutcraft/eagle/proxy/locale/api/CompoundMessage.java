package nl.scoutcraft.eagle.proxy.locale.api;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public record CompoundMessage(Internationalization lang, String baseKey, IPlaceholder... placeholders) implements IMessage {

    @Override
    public String getString(@Nullable Locale locale, IPlaceholder... placeholders) {
        return this.apply(this.apply(this.lang.getString(locale, this.baseKey), locale, this.placeholders), locale, placeholders);
    }

    @Override
    public Component get(@Nullable Locale locale, boolean colorized, IPlaceholder... placeholders) {
        return this.apply(this.apply(colorized ? TextUtils.colorize(this.lang.getString(locale, this.baseKey)) : Component.text(this.lang.getString(locale, this.baseKey)), locale, placeholders), locale, this.placeholders);
    }
}
