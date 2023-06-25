package nl.scoutcraft.eagle.server.gui.inventory.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractInventoryMenu implements IInventoryMenu {

    protected final Map<Integer, Button> buttons;

    private Component title;
    private InventoryType type;
    private int size;
    private int modifierInterval;

    @Nullable private Inventory inv;
    @Nullable private ModifierTask modifierTask;

    public AbstractInventoryMenu() {
        this.buttons = Maps.newHashMap();
        this.title = Component.empty();
        this.type = InventoryType.CHEST;
        this.size = 27;
        this.modifierInterval = 60;
    }

    @Override
    public void open(Player player) {
        if (this.inv == null) {
            this.inv = this.type == InventoryType.CHEST ? Bukkit.createInventory(this, this.size, this.title) : Bukkit.createInventory(this, this.type, this.title);
            this.apply();
        }

        Bukkit.getScheduler().runTaskLater(EagleServer.getInstance(), () -> player.openInventory(this.inv), 0L);
    }

    @Override
    public void update() {
        this.apply();
    }

    protected void apply() {
        if (this.inv == null)
            return;

        if (this.modifierTask != null) {
            this.modifierTask.stop();
            this.modifierTask = null;
        }

        this.inv.clear();
        this.buttons.clear();

        int id = 0;
        for (Button button : this.getButtons()) {
            ItemStack is = this instanceof AbstractPlayerInventoryMenu apim ? button.build(id, apim.player) : button.build(id);
            for (int index : button.getSlots()) {
                if (index == -1) this.inv.addItem(is);
                else this.inv.setItem(index, is);
            }

            this.buttons.put(id++, button);

            if (button.getModifier() != null) {
                if (this.modifierTask == null)
                    this.modifierTask = new ModifierTask(this.modifierInterval, this.inv);
                this.modifierTask.addModifier(button);
            }
        }

        if (this.modifierTask != null)
            this.modifierTask.start();
    }

    @Override
    public void close() {
        this.close(null);
    }

    public void close(@Nullable Consumer<Player> action) {
        if (this.inv != null) {
            Lists.newArrayList(this.inv.getViewers()).forEach(h -> { h.closeInventory(); if (action != null) action.accept((Player) h); }); // Makes a new list to prevent ConcurrentModificationException's
            this.inv = null;
        }

        if (this.modifierTask != null) {
            this.modifierTask.stop();
            this.modifierTask = null;
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);

        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null)
            return;

        Integer id = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER);
        if (id == null)
            return;

        Button button = this.buttons.get(id);
        if (button != null)
            button.executeActions(e.getClick(), (Player) e.getWhoClicked());
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inv;
    }

    @Override
    public void setTitle(String title) {
        this.title = TextUtils.colorize(title);
    }

    @Override
    public void setTitle(Component title) {
        this.title = title;
    }

    @Override
    public void setType(InventoryType type) {
        this.type = type;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void setModifierInterval(int ticks) {
        this.modifierInterval = ticks;
    }

    protected abstract List<Button> getButtons();
}
