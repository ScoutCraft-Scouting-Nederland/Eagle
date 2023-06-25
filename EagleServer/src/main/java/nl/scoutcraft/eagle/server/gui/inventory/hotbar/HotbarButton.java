package nl.scoutcraft.eagle.server.gui.inventory.hotbar;

import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import nl.scoutcraft.eagle.server.gui.inventory.IInventoryMenuManager;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class HotbarButton implements IHotbarButton {

    private static int counter = 0;

    private final int id;
    private final ItemBuilder item;
    private final int slot;
    @Nullable private final Consumer<Player> action;
    @Nullable private final Predicate<Player> cancellableAction;
    private final int cooldown;
    private final boolean removeOnUse;
    private final boolean movable;

    public HotbarButton(ItemBuilder item, int slot, @Nullable Consumer<Player> action, @Nullable Predicate<Player> cancellableAction, int cooldown, boolean removeOnUse, boolean movable) {
        this.id = counter++;
        this.item = item;
        this.slot = slot;
        this.action = action;
        this.cancellableAction = cancellableAction;
        this.cooldown = cooldown;
        this.removeOnUse = removeOnUse;
        this.movable = movable;

        this.item.data(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER, this.id);
        if (!movable) this.item.data(EagleKeys.BUTTON_IMMOVABLE, PersistentDataType.BYTE, (byte) 1);
    }

    public int getId() {
        return this.id;
    }

    public ItemBuilder getItem() {
        return this.item;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public boolean isRemoveOnUse() {
        return this.removeOnUse;
    }

    public boolean isMovable() {
        return this.movable;
    }

    public void apply(Player player) {
        if (this.slot > -1) {
            player.getInventory().setItem(this.slot, this.item.build(player));
        } else {
            player.getInventory().addItem(this.item.build(player));
        }
    }

    public boolean hasAction() {
        return this.action != null || this.cancellableAction != null;
    }

    public boolean execute(Player player) {
        if (this.cancellableAction != null)
            return this.cancellableAction.test(player);

        if (this.action != null)
            this.action.accept(player);
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ItemBuilder item;
        private int slot = -1;
        @Nullable private Consumer<Player> action;
        @Nullable private Predicate<Player> cancellableAction;
        private int cooldown = 0;
        private boolean removeOnUse = false;
        private boolean moveable = false;

        /**
         * Sets the item displayed by the button.
         * Required for a valid {@link HotbarButton}.
         *
         * @param item The {@link ItemBuilder}.
         * @return This {@link Builder} for chaining.
         */
        public Builder setItem(ItemBuilder item) {
            this.item = item;
            return this;
        }

        /**
         * Sets the slot the button will be placed in. (0 to 8)
         *
         * @param slot The slot.
         * @return This {@link Builder} for chaining.
         */
        public Builder setSlot(int slot) {
            this.slot = slot;
            return this;
        }

        /**
         * Adds an action to the {@link HotbarButton}.
         * If no action is provided, the event will NOT be cancelled.
         *
         * @param action The action.
         * @return This {@link Builder} for chaining.
         */
        public Builder setAction(@Nullable Consumer<Player> action) {
            this.action = action;
            return this;
        }

        /**
         * Adds an action to the {@link HotbarButton}.
         * The boolean return value of the {@link Predicate} decides whether cooldown should be applied and consumables should be removed.
         * If no action is provided, the event will NOT be cancelled.
         *
         * @param action The action.
         * @return This {@link Builder} for chaining.
         */
        public Builder setAction(@Nullable Predicate<Player> action) {
            this.cancellableAction = action;
            return this;
        }

        /**
         * Sets cooldown of the button. Displays as vanilla enderpearl cooldown.
         * Default is 0.
         *
         * @param cooldown The cooldown in ticks.
         * @return This {@link Builder} for chaining.
         */
        public Builder setCooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        /**
         * Allows {@link HotbarButton}'s to be used as consumables.
         * Default is false.
         *
         * @param value Whether or not to remove the item on use.
         * @return This {@link Builder} for chaining.
         */
        public Builder setRemoveOnUse(boolean value) {
            this.removeOnUse = value;
            return this;
        }

        /**
         * Allows {@link HotbarButton}'s to be moved around in a players inventory. It can not be dropped, swapped to offhand,
         * Default is false.
         *
         * @param moveable Whether the button can be moved.
         * @return This {@link Builder} for chaining.
         */
        public Builder setMoveable(boolean moveable) {
            this.moveable = moveable;
            return this;
        }

        /**
         * Build the {@link HotbarButton}.
         *
         * @param register Whether to register the button's action with the {@link IInventoryMenuManager}
         * @return the button.
         */
        public HotbarButton build(boolean register) {
            HotbarButton button = new HotbarButton(this.item, this.slot, this.action, this.cancellableAction, this.cooldown, this.removeOnUse, this.moveable);
            if (register)
                EagleServer.getInstance().getInventoryMenuManager().register(button);

            return button;
        }
    }
}
