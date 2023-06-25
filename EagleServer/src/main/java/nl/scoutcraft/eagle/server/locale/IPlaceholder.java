package nl.scoutcraft.eagle.server.locale;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface IPlaceholder {

    default Component apply(Component comp) {
        return this.apply(comp, (Locale) null);
    }
    Component apply(Component comp, @Nullable Player player);
    Component apply(Component comp, @Nullable Locale locale);

    default String apply(String str) {
        return apply(str, (Locale) null);
    }
    String apply(String str, @Nullable Player player);
    String apply(String str, @Nullable Locale locale);
}
