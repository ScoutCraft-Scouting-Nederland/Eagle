package nl.scoutcraft.eagle.server.gui.inventory.base;

import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public enum ButtonClickType implements Predicate<ClickType> {

    LEFT(true, false, false),
    RIGHT(false, false, false),
    MIDDLE(null, null, true),
    LEFT_SHIFT(true, true, false),
    RIGHT_SHIFT(false, true, false),
    ANY_LEFT(true, null, false),
    ANY_RIGHT(false, null, false),
    ANY_SHIFT(null, true, null),
    ANY_NON_SHIFT(null, false, null),
    ANY(null, null, null);

    @Nullable private final Boolean left;
    @Nullable private final Boolean shift;
    @Nullable private final Boolean middle;

    ButtonClickType(@Nullable Boolean left, @Nullable Boolean shift, @Nullable Boolean middle) {
        this.left = left;
        this.shift = shift;
        this.middle = middle;
    }

    @Override
    public boolean test(ClickType clickType) {
        if (this.middle != null && this.middle != (clickType == ClickType.MIDDLE))
            return false;

        if (this.shift != null && this.shift != clickType.isShiftClick())
            return false;

        return this.left == null || ((this.left || !clickType.isLeftClick()) && (!this.left || !clickType.isRightClick()));
    }
}
