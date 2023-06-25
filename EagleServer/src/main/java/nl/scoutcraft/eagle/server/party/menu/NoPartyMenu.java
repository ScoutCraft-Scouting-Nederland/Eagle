package nl.scoutcraft.eagle.server.party.menu;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.eagle.libs.party.PartyInvitation;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.gui.inventory.base.AbstractInventoryMenu;
import nl.scoutcraft.eagle.server.gui.inventory.base.Button;
import nl.scoutcraft.eagle.server.gui.inventory.base.ButtonClickType;
import nl.scoutcraft.eagle.server.locale.Messages;
import nl.scoutcraft.eagle.server.party.PartyManager;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class NoPartyMenu extends AbstractInventoryMenu {

    private final PartyManager manager;

    public NoPartyMenu(PartyManager manager) {
        super.setTitle("Party Menu");
        super.setType(InventoryType.CHEST);
        super.setSize(27);

        this.manager = manager;
    }

    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(Button.spacer(IntStream.range(0, 27).toArray()).build());

        buttons.add(Button.builder()
                .setSlots(11)
                .setItem(new ItemBuilder(Material.SLIME_BALL, 1).name(Component.text("Create", Colors.GOLD)))
                .addAction(ButtonClickType.ANY, p -> this.manager.sendCommand(p, "party create"))
                .build());

        buttons.add(Button.builder()
                .setSlots(14)
                .setItem(new ItemBuilder(Material.NETHER_STAR, 1).name(Component.text("Join Invitation", Colors.GOLD)))
                .addAction(ButtonClickType.ANY, p -> this.manager.getPartyChannel()
                        .<List<PartyInvitation>>request()
                        .onResponse(data -> new InvitesMenu(p, this.manager, data).open())
                        .setTarget(p)
                        .getInvites(p.getUniqueId()))
                .build());

        buttons.add(Button.builder()
                .setSlots(15)
                .setItem(new ItemBuilder(Material.NETHER_STAR, 1).name(Component.text("Join Public Party", Colors.GOLD)))
                .addAction(ButtonClickType.ANY, Messages.PARTY_JOIN_PUBLIC::send)
                .build());

        return buttons;
    }
}
