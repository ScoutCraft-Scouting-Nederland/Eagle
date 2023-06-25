package nl.scoutcraft.eagle.server.gui.inventory;

import nl.scoutcraft.eagle.server.gui.inventory.hotbar.IHotbarButton;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

public interface IInventoryMenuManager extends Listener {

    /**
     * Adds the buttons actions to the listener.
     *
     * @param buttons The {@link IHotbarButton} instances.
     * @return The {@link IInventoryMenuManager} instance for chaining.
     */
    IInventoryMenuManager register(IHotbarButton... buttons);

    /**
     * Removes the buttons actions from the listener.
     *
     * @param buttons The {@link IHotbarButton} instances.
     * @return The {@link IInventoryMenuManager} instance for chaining.
     */
    IInventoryMenuManager unregister(IHotbarButton... buttons);

    /**
     * Gets an active {@link IHotbarButton}.
     *
     * @param id The id of the button.
     * @return The {@link IHotbarButton} instance.
     */
    @Nullable
    IHotbarButton get(int id);

    /**
     * Removes all active {@link IHotbarButton}'s from the players inventory.
     *
     * @param player The {@link Player} instance.
     * @return The {@link IInventoryMenuManager} instance for chaining.
     */
    IInventoryMenuManager clear(Player player);

    /**
     * Adds the buttons to the players inventory.
     * WARNING: Buttons must be registered first before their actions will work.
     *
     * @param player The {@link Player} instance.
     * @param buttons The {@link IHotbarButton}'s to give to the player.
     * @return The {@link IInventoryMenuManager} instance for chaining.
     */
    default IInventoryMenuManager give(Player player, IHotbarButton... buttons) {
        for (IHotbarButton button : buttons)
            button.apply(player);
        return this;
    }
}
