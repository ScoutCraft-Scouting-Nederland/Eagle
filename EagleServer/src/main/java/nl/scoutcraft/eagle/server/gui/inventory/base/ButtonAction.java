package nl.scoutcraft.eagle.server.gui.inventory.base;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ButtonAction {

    private final ButtonClickType clickType;
    private final Consumer<Player> action;

    public ButtonAction(ButtonClickType clickType, Consumer<Player> action) {
        this.clickType = clickType;
        this.action = action;
    }

    public ButtonClickType getClickType() {
        return clickType;
    }

    public Consumer<Player> getAction() {
        return action;
    }
}
