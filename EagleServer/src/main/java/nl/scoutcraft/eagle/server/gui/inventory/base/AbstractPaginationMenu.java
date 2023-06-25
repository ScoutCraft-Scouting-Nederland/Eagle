package nl.scoutcraft.eagle.server.gui.inventory.base;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractPaginationMenu extends AbstractPlayerInventoryMenu {

    private List<Button> buttons;
    private int rows;
    private int row;

    public AbstractPaginationMenu(Player player) {
        super(player);
        super.setSize(54);
    }

    @Override
    protected List<Button> getButtons(Player player) {
        if (this.buttons == null) {
            this.buttons = this.getListButtons(player);
            this.rows = this.buttons.size() / 9 + 1;
            this.row = 1;
        }

        List<Button> buttons = new ArrayList<>();

        // Bottom Row - Buttons
        // Slot 1-9: Spacers
        buttons.add(Button.spacer(IntStream.range(45, 54).toArray()).build());

        // Slot 2: Previous Page
        if (this.row > 1)
            buttons.add(Button.builder()
                    .setSlots(46)
                    .setItem(new ItemBuilder(Material.ARROW).name(Component.text("Scroll Up", Colors.GOLD)).lore(Component.text("Click to scroll up 1 row", Colors.GRAY), Component.text("Shift-click to scroll up 5 rows", Colors.GRAY)))
                    .addAction(ButtonClickType.ANY_NON_SHIFT, p -> this.scroll(-1))
                    .addAction(ButtonClickType.ANY_SHIFT, p -> this.scroll(-5))
                    .build());

        // Slot 8: Next Page
        if (this.rows > 4 && this.row < this.rows - 4)
            buttons.add(Button.builder()
                    .setSlots(52)
                    .setItem(new ItemBuilder(Material.ARROW).name(Component.text("Scroll Down", Colors.GOLD)).lore(Component.text("Click to scroll down 1 row", Colors.GRAY), Component.text("Shift-click to scroll down 5 rows", Colors.GRAY)))
                    .addAction(ButtonClickType.ANY_NON_SHIFT, p -> this.scroll(1))
                    .addAction(ButtonClickType.ANY_SHIFT, p -> this.scroll(5))
                    .build());

        buttons.addAll(this.getMenuButtons(player));

        int startIndex = (this.row - 1) * 9;
        for (int i = startIndex; i < startIndex + 45 && i < this.buttons.size(); i++)
            buttons.add(this.buttons.get(i));

        return buttons;
    }

    @Override
    public void update() {
        this.buttons = null;
        super.update();
    }

    public void scroll(int rows) {
        this.row = Math.max(1, Math.min(this.rows, this.row + rows));
        if (this.rows > 4 && this.row > this.rows - 4)
            this.row = this.rows - 4;

        super.getInventory().clear();
        super.apply();
    }

    protected abstract List<Button> getListButtons(Player player);
    protected abstract List<Button> getMenuButtons(Player player);
}
