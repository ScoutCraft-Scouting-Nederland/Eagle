package nl.scoutcraft.eagle.server.party.menu;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.party.PartyInvitation;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.gui.inventory.base.AbstractPaginationMenu;
import nl.scoutcraft.eagle.server.gui.inventory.base.Button;
import nl.scoutcraft.eagle.server.gui.inventory.base.ButtonClickType;
import nl.scoutcraft.eagle.server.party.PartyManager;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InvitesMenu extends AbstractPaginationMenu {

    private final PartyManager manager;
    private final List<PartyInvitation> invites;

    public InvitesMenu(Player player, PartyManager partyManager, List<PartyInvitation> invites) {
        super(player);
        super.setTitle("Invites Menu");

        this.manager = partyManager;
        this.invites = invites;
    }

    @Override
    protected List<Button> getListButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < this.invites.size(); i++) {
            PartyInvitation invite = this.invites.get(i);
            buttons.add(Button.builder()
                    .setSlots(i)
                    .setItem(new ItemBuilder(Material.PLAYER_HEAD).skull(invite.getTextureProperty()).name(Component.text(invite.getName(), Colors.GOLD)))
                    .addAction(ButtonClickType.ANY, p -> {
                        p.closeInventory();
                        this.manager.sendCommand(p, "party join " + invite.getName());
                    })
                    .build());
        }

        return buttons;
    }

    @Override
    protected List<Button> getMenuButtons(Player player) {
        return Lists.newArrayList(Button.builder()
                .setSlots(49)
                .setItem(new ItemBuilder(Material.BARRIER).name(Component.text("Back", Colors.RED)))
                .addAction(ButtonClickType.ANY, this.manager.getNoPartyMenu()::open)
                .build());
    }
}
