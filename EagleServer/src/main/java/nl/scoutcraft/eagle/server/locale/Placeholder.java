package nl.scoutcraft.eagle.server.locale;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public record Placeholder(String key, String value) implements IPlaceholder {

    @Override
    public Component apply(Component comp) {
        return comp.replaceText(b -> b.matchLiteral(this.key).replacement(this.value));
    }

    @Override
    public Component apply(Component comp, @Nullable Player player) {
        return apply(comp);
    }

    @Override
    public Component apply(Component comp, @Nullable Locale locale) {
        return apply(comp);
    }

    @Override
    public String apply(String str) {
        return str.replace(this.key, this.value);
    }

    @Override
    public String apply(String str, @Nullable Player player) {
        return this.apply(str);
    }

    @Override
    public String apply(String str, @Nullable Locale locale) {
        return this.apply(str);
    }
}
