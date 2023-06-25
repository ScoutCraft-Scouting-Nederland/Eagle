package nl.scoutcraft.eagle.server.party.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.eagle.libs.party.PartyMemberInfo;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.gui.inventory.base.Button;
import nl.scoutcraft.eagle.server.gui.inventory.base.ButtonClickType;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PartyTransferMenu extends BasePartyMemberMenu {

    public PartyTransferMenu(Player player, PartyMenu partyMenu, List<PartyMemberInfo> members, UUID partyId) {
        super(player, partyMenu, members, partyId);
        super.setTitle("Party Transfer Menu");
    }

    @Override
    protected Button getButton(int slot, PartyMemberInfo member) {
        return Button.builder()
                .setSlots(slot)
                .setItem(new ItemBuilder(Material.PLAYER_HEAD).skull(member.getUuid(), member.getTexture()).name(Component.text(member.getName(), Colors.GOLD, TextDecoration.BOLD)).lore(Component.text("Click to transfer the party to " + member.getName(), Colors.GRAY)))
                .addAction(ButtonClickType.ANY, p -> {
                    super.partyMenu.getManager().sendCommand(p, "party transfer " + member.getName());
                    super.partyMenu.open(p);
                })
                .build();
    }
}
