package nl.scoutcraft.eagle.server.party.menu;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.party.PartyInfo;
import nl.scoutcraft.eagle.libs.party.PartyMemberInfo;
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
import java.util.UUID;
import java.util.stream.IntStream;

public class PartyMenu extends AbstractInventoryMenu implements IPartyMenu {

    private static final ItemBuilder PUBLIC = new ItemBuilder(Material.LIME_DYE, 1).name(Component.text("Status: Public", Colors.GOLD)).lore(Component.text("Click to make private.", Colors.GRAY));
    private static final ItemBuilder PRIVATE = new ItemBuilder(Material.GRAY_DYE, 1).name(Component.text("Status: Private", Colors.GOLD)).lore(Component.text("Click to make public.", Colors.GRAY));

    private final PartyManager manager;
    private PartyInfo party;

    public PartyMenu(PartyManager manager, PartyInfo party) {
        super.setTitle("Party Menu");
        super.setType(InventoryType.CHEST);
        super.setSize(45);

        this.manager = manager;
        this.party = party;
    }

    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(Button.spacer(IntStream.range(0, 45).toArray()).build());

        buttons.add(Button.builder()
                .setSlots(11)
                .setItem(new ItemBuilder(Material.PLAYER_HEAD, 1).name(Component.text("Info", Colors.GOLD)))
                .addAction(ButtonClickType.ANY, p -> {
                    p.closeInventory();
                    this.manager.sendCommand(p, "party info");
                })
                .build());

        buttons.add(Button.builder()
                .setSlots(12)
                .setItem(this.party.isPublicParty() ? PUBLIC : PRIVATE)
                .addAction(ButtonClickType.ANY, p -> {
                    this.manager.sendCommand(p, "party " + (this.party.isPublicParty() ? "private" : "public"));
                    this.party = new PartyInfo(this.party.getPartyId(), !this.party.isPublicParty(), this.party.getLeader(), this.party.getMembers());
                    this.update();
                })
                .build());

        buttons.add(Button.builder()
                .setSlots(15)
                .setItem(new ItemBuilder(Material.FIREWORK_ROCKET, 1).name(Component.text("Leave", Colors.GOLD)).lore(Component.text("Click to leave the party.", Colors.GRAY)))
                .addAction(ButtonClickType.ANY, p -> {
                    p.closeInventory();
                    this.manager.sendCommand(p, "party leave");
                })
                .build());

        buttons.add(Button.builder()
                .setSlots(24)
                .setItem(new ItemBuilder(Material.GOLD_NUGGET, 1).name(Component.text("Transfer", Colors.GOLD)).lore(Component.text("Click to transfer the party.", Colors.GRAY)))
                .addAction(ButtonClickType.ANY, p -> {
                    this.manager.getPartyChannel()
                            .<List<PartyMemberInfo>>request()
                            .onResponse(data -> new PartyTransferMenu(p, this, data, this.getPartyId()).open())
                            .timeout(175)
                            .onTimeout(() -> {
                                p.closeInventory();
                                Messages.PARTY_ENTER_NAME.send(p);
                                this.manager.sendPartyAction("transfer", p);
                            })
                            .setTarget(p)
                            .getMembers(p.getUniqueId());
                })
                .build());

        buttons.add(Button.builder()
                .setSlots(29)
                .setItem(new ItemBuilder(Material.ENDER_EYE, 1).name(Component.text("Invite", Colors.GOLD)).lore(Component.text("Click to invite someone to the party.", Colors.GRAY)))
                .addAction(ButtonClickType.ANY, p -> {
                    p.closeInventory();
                    Messages.PARTY_ENTER_NAME.send(p);
                    this.manager.sendPartyAction("invite", p);
                })
                .build());

        buttons.add(Button.builder()
                .setSlots(30)
                .setItem(new ItemBuilder(Material.GHAST_TEAR, 1).name(Component.text("Kick", Colors.GOLD)).lore(Component.text("Click to kick someone from the party.", Colors.GRAY)))
                .addAction(ButtonClickType.ANY, p -> {
                    this.manager.getPartyChannel()
                            .<List<PartyMemberInfo>>request()
                            .onResponse(data -> new PartyKickMenu(p, this, data, this.getPartyId()).open())
                            .timeout(175)
                            .onTimeout(() -> {
                                p.closeInventory();
                                Messages.PARTY_ENTER_NAME.send(p);
                                this.manager.sendPartyAction("kick", p);
                            })
                            .setTarget(p)
                            .getMembers(p.getUniqueId());
                })
                .build());

        buttons.add(Button.builder()
                .setSlots(31)
                .setItem(new ItemBuilder(Material.SOUL_LANTERN, 1).name(Component.text("Warp", Colors.GOLD)).lore(Component.text("Click to warp all party members to you.", Colors.GRAY)))
                .addAction(ButtonClickType.ANY, p -> {
                    p.closeInventory();
                    this.manager.sendCommand(p, "party warp");
                })
                .build());

        buttons.add(Button.builder()
                .setSlots(33)
                .setItem(new ItemBuilder(Material.BARRIER, 1).name(Component.text("Disband", Colors.GOLD)).lore(Component.text("Click to disband the party.", Colors.GRAY)))
                .addAction(ButtonClickType.ANY, p -> {
                    p.closeInventory();
                    this.manager.sendCommand(p, "party disband");
                })
                .build());

        return buttons;
    }

    public PartyManager getManager() {
        return this.manager;
    }

    public PartyInfo getParty() {
        return this.party;
    }

    @Override
    public UUID getPartyId() {
        return this.party.getPartyId();
    }
}
