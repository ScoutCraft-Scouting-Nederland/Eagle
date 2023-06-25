package nl.scoutcraft.eagle.proxy.locale.api;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public record ComponentPlaceholder(String key, Component value) implements IPlaceholder {

    @Override
    public Component apply(Component comp) {
        return comp.replaceText(b -> b.matchLiteral(this.key).replacement(this.value));
    }

    @Override
    public Component apply(Component comp, @Nullable Player player) {
        return this.apply(comp);
    }

    @Override
    public Component apply(Component comp, @Nullable Locale locale) {
        return this.apply(comp);
    }

    @Override
    public String apply(String str) {
        return str.replace(this.key, TextUtils.toString(this.value));
    }

    @Override
    public String apply(String str, @Nullable Player player) {
        return apply(str);
    }

    @Override
    public String apply(String str, @Nullable Locale locale) {
        return apply(str);
    }
}
