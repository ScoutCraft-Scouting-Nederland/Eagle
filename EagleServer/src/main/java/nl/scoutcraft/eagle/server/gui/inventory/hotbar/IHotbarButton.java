package nl.scoutcraft.eagle.server.gui.inventory.hotbar;

import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.entity.Player;

public interface IHotbarButton {

    int getId();

    ItemBuilder getItem();

    int getSlot();

    int getCooldown();

    boolean isRemoveOnUse();

    boolean isMovable();

    void apply(Player player);

    boolean hasAction();

    boolean execute(Player player);
}
