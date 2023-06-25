package nl.scoutcraft.eagle.server.gui.inventory.base;

import com.google.common.collect.Lists;
import nl.scoutcraft.eagle.server.EagleServer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class ModifierTask implements Runnable {

    private final int interval;

    private final List<Button> modifiers;
    private Inventory inv;
    private BukkitTask task;

    public ModifierTask(int interval, Inventory inv) {
        this.interval = interval;

        this.modifiers = Lists.newArrayList();
        this.inv = inv;
        this.task = null;
    }

    public void addModifier(Button button) {
        this.modifiers.add(button);
    }

    public void start() {
        if (this.task != null)
            this.task.cancel();

        this.task = Bukkit.getScheduler().runTaskTimer(EagleServer.getInstance(), this, this.interval, this.interval);
    }

    @Override
    public void run() {
        if (this.inv.getViewers().isEmpty())
            return;

        for (Button button : this.modifiers) {
            ItemStack item = this.inv.getItem(button.getSlots()[0]);
            if (item == null)
                continue;

            button.getModifier().accept(item);
            for (int slot : button.getSlots())
                this.inv.setItem(slot, item);
        }
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        this.modifiers.clear();
        this.inv = null;
    }
}
