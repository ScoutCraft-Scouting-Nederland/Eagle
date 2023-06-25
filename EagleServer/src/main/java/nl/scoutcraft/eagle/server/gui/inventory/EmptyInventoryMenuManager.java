package nl.scoutcraft.eagle.server.gui.inventory;

import nl.scoutcraft.eagle.server.gui.inventory.hotbar.IHotbarButton;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class EmptyInventoryMenuManager implements IInventoryMenuManager {

    @Override
    public IInventoryMenuManager register(IHotbarButton... buttons) {
        return this;
    }

    @Override
    public IInventoryMenuManager unregister(IHotbarButton... buttons) {
        return this;
    }

    @Override
    @Nullable
    public IHotbarButton get(int id) {
        return null;
    }

    @Override
    public IInventoryMenuManager clear(Player player) {
        return this;
    }
}
