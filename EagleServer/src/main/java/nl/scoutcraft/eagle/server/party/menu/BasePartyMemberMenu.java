package nl.scoutcraft.eagle.server.party.menu;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.party.PartyMemberInfo;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.gui.inventory.base.AbstractPaginationMenu;
import nl.scoutcraft.eagle.server.gui.inventory.base.Button;
import nl.scoutcraft.eagle.server.gui.inventory.base.ButtonClickType;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BasePartyMemberMenu extends AbstractPaginationMenu implements IPartyMenu {

    protected final PartyMenu partyMenu;
    private final List<PartyMemberInfo> members;
    private final UUID partyId;

    public BasePartyMemberMenu(Player player, PartyMenu partyMenu, List<PartyMemberInfo> members, UUID partyId) {
        super(player);
        super.setTitle("");

        this.partyMenu = partyMenu;
        this.members = members;
        this.partyId = partyId;
    }

    @Override
    protected List<Button> getListButtons(Player player) {
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < this.members.size(); i++)
            buttons.add(this.getButton(i, this.members.get(i)));

        return buttons;
    }

    @Override
    protected List<Button> getMenuButtons(Player player) {
        return Lists.newArrayList(Button.builder()
                .setSlots(49)
                .setItem(new ItemBuilder(Material.BARRIER).name(Component.text("Back", Colors.RED)))
                .addAction(ButtonClickType.ANY, this.partyMenu::open)
                .build());
    }

    protected abstract Button getButton(int slot, PartyMemberInfo member);

    @Override
    public UUID getPartyId() {
        return this.partyId;
    }
}
