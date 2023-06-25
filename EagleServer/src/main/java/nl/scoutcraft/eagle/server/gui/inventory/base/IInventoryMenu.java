package nl.scoutcraft.eagle.server.gui.inventory.base;

import nl.scoutcraft.eagle.server.gui.IMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

public interface IInventoryMenu extends IMenu, InventoryHolder {

    void setType(InventoryType type);
    void setSize(int size);
    void setModifierInterval(int ticks);

    void onInventoryClick(InventoryClickEvent e);
    default void onInventoryClose(Player player) {}
}
