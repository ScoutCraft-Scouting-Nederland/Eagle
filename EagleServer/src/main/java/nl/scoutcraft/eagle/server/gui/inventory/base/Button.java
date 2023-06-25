package nl.scoutcraft.eagle.server.gui.inventory.base;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Button {

    private final ItemBuilder item;
    private final int[] slots;
    private final List<ButtonAction> actions;

    @Nullable private final Consumer<ItemStack> modifier;

    public Button(ItemBuilder item, int[] slots, List<ButtonAction> actions, @Nullable Consumer<ItemStack> modifier) {
        this.item = item;
        this.slots = slots;
        this.actions = actions;
        this.modifier = modifier;
    }

    public int[] getSlots() {
        return slots;
    }

    @Nullable
    public Consumer<ItemStack> getModifier() {
        return modifier;
    }

    protected ItemStack build(int id) {
        return this.build(id, null);
    }

    protected ItemStack build(int id, Player player) {
        return this.item.data(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER, id).build(player);
    }

    protected void executeActions(ClickType clickType, Player player) {
        this.actions.stream().filter(a -> a.getClickType().test(clickType)).forEach(a -> a.getAction().accept(player));
    }

    public static Builder spacer() {
        return spacer(Material.BLACK_STAINED_GLASS_PANE);
    }

    public static Builder spacer(Material mat) {
        return builder().setItem(new ItemBuilder(mat).name(Component.text(' ')));
    }

    public static Builder spacer(int... indices) {
        return spacer().setSlots(indices);
    }

    public static Builder spacer(Material mat, int... indices) {
        return spacer(mat).setSlots(indices);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ItemBuilder item;
        private int[] slots;
        private final List<ButtonAction> actions;

        @Nullable private Consumer<ItemStack> modifier;

        public Builder() {
            this.slots = new int[]{-1};
            this.actions = new ArrayList<>();
            this.modifier = null;
        }

        public Builder setItem(ItemBuilder item) {
            this.item = item;
            return this;
        }

        public Builder setSlots(int... slots) {
            this.slots = slots;
            return this;
        }

        @Deprecated
        public Builder setIndices(int... indices) {
            return this.setSlots(indices);
        }

        public Builder addAction(ButtonClickType clickType, Consumer<Player> action) {
            this.actions.add(new ButtonAction(clickType, action));
            return this;
        }

        @Deprecated
        public Builder setActions(@Nullable Consumer<Player> action) {
            return this.addAction(ButtonClickType.ANY, action);
        }

        @Deprecated
        public Builder setLeftAction(@Nullable Consumer<Player> action) {
            return this.addAction(ButtonClickType.ANY_LEFT, action);
        }

        @Deprecated
        public Builder setRightAction(@Nullable Consumer<Player> action) {
            return this.addAction(ButtonClickType.ANY_RIGHT, action);
        }

        public Builder setModifier(@Nullable Consumer<ItemStack> modifier) {
            this.modifier = modifier;
            return this;
        }

        public Button build() {
            return new Button(this.item, this.slots, this.actions, this.modifier);
        }
    }
}
