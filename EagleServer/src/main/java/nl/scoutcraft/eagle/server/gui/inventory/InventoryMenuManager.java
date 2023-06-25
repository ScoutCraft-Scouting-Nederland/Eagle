package nl.scoutcraft.eagle.server.gui.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import nl.scoutcraft.eagle.server.gui.inventory.base.IInventoryMenu;
import nl.scoutcraft.eagle.server.gui.inventory.hotbar.IHotbarButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class InventoryMenuManager implements IInventoryMenuManager {

    private final Map<Integer, IHotbarButton> buttons;

    public InventoryMenuManager() {
        this.buttons = new Int2ObjectArrayMap<>();
    }

    @Override
    public IInventoryMenuManager register(IHotbarButton... buttons) {
        for (IHotbarButton button : buttons)
            this.buttons.put(button.getId(), button);
        return this;
    }

    @Override
    public IInventoryMenuManager unregister(IHotbarButton... buttons) {
        for (IHotbarButton button : buttons)
            this.buttons.remove(button.getId());
        return this;
    }

    @Override
    @Nullable
    public IHotbarButton get(int id) {
        return this.buttons.get(id);
    }

    @Override
    public IInventoryMenuManager clear(Player player) {
        PlayerInventory inv = player.getInventory();
        for (ItemStack is : inv.getContents()) {
            if (is != null && is.getItemMeta() != null && is.getItemMeta().getPersistentDataContainer().has(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER))
                inv.remove(is);
        }
        return this;
    }

    /**
     * At the time of testing (22/11/2020), getting the {@link InventoryHolder} from custom inventories does not work on spigot, but does work on paper.
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof IInventoryMenu)
            ((IInventoryMenu) holder).onInventoryClose((Player) e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventoryClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof IInventoryMenu)
            ((IInventoryMenu) holder).onInventoryClick(e);

        // Prevents immovable hotbar buttons from being moved around
        if (this.isImmovable(e.getCurrentItem()))
            e.setCancelled(true);
        else if (e.getClick() == ClickType.NUMBER_KEY && this.isImmovable(e.getWhoClicked().getInventory().getItem(e.getHotbarButton())))
            e.setCancelled(true);
    }

    private boolean isImmovable(ItemStack is) {
        return is != null && is.getItemMeta() != null && is.getItemMeta().getPersistentDataContainer().has(EagleKeys.BUTTON_IMMOVABLE, PersistentDataType.BYTE);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent e) {
        InventoryHolder holder = e.getPlayer().getOpenInventory().getTopInventory().getHolder();
        if (holder instanceof IInventoryMenu)
            ((IInventoryMenu) holder).onInventoryClose(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL || e.getItem() == null || e.getHand() != EquipmentSlot.HAND)
            return;

        ItemStack is = e.getItem();
        if (is == null)
            return;

        Integer id = is.getItemMeta().getPersistentDataContainer().get(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER);
        if (id == null)
            return;

        IHotbarButton button = this.buttons.get(id);
        if (button == null)
            return;

        Player player = e.getPlayer();
        if (button.getCooldown() > 0 && player.getCooldown(is.getType()) > 0)
            return;

        if (button.hasAction()) {
            e.setCancelled(true);

            if (!button.execute(player))
                return;
        }

        if (button.getCooldown() > 0)
            player.setCooldown(is.getType(), button.getCooldown());

        if (button.isRemoveOnUse()) {
            if (is.getAmount() > 1) {
                is.setAmount(is.getAmount() - 1);
                player.getInventory().setItemInMainHand(is);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSwap(PlayerSwapHandItemsEvent e) {
        ItemStack main = e.getMainHandItem();
        ItemStack off = e.getOffHandItem();

        if (main != null && main.getItemMeta() != null && main.getItemMeta().getPersistentDataContainer().has(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER))
            e.setCancelled(true);
        else if (off != null && off.getItemMeta() != null && off.getItemMeta().getPersistentDataContainer().has(EagleKeys.BUTTON_ID, PersistentDataType.INTEGER))
            e.setCancelled(true);
    }
}
