package nl.scoutcraft.eagle.server.locale;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public record MessagePlaceholder(String key, IMessage value) implements IPlaceholder {

    @Override
    public Component apply(Component comp, @Nullable Player player) {
        return comp.replaceText(b -> b.matchLiteral(this.key).replacement(this.value.get(player)));
    }

    @Override
    public Component apply(Component comp, @Nullable Locale locale) {
        return comp.replaceText(b -> b.matchLiteral(this.key).replacement(this.value.get(locale)));
    }

    @Override
    public String apply(String str, @Nullable Player player) {
        return str.replace(this.key, this.value.getString(player));
    }

    @Override
    public String apply(String str, @Nullable Locale locale) {
        return str.replace(this.key, this.value.getString(locale));
    }
}
