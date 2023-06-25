package nl.scoutcraft.eagle.server.gui.inventory.base;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractPlayerInventoryMenu extends AbstractInventoryMenu {

    protected final Player player;

    public AbstractPlayerInventoryMenu(Player player) {
        this.player = player;
    }

    @Override
    protected List<Button> getButtons() {
        return this.getButtons(this.player);
    }

    public void open() {
        super.open(this.player);
    }

    protected abstract List<Button> getButtons(Player player);
}
