package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

import java.util.stream.IntStream;

public class Info extends PartySubCommand {

    public Info(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_INFO, "", Perms.PARTY_INFO, "info", "list");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Party party = super.partyManager.getParty(player);
        if (party == null) {
            PartyMessages.NOT_IN_PARTY.send(player);
            return;
        }

        StringBuilder memberList = new StringBuilder();

        if (party.getMembers().isEmpty()) {
            memberList.append(" ");
        } else {
            memberList.append(party.getMembers().get(0));
            IntStream.range(1, party.getMembers().size()).forEach(i -> memberList.append(Colors.DARK_GRAY).append(", ").append(Colors.GRAY).append(party.getMembers().get(i).getUsername()));
        }

        Component leader = PartyMessages.LEADER.get(player, true).append(Component.text(" » ", Colors.DARK_GRAY)).append(Component.text(party.getLeader().getUsername(), Colors.RED));
        Component members = PartyMessages.MEMBERS.get(player, true).append(Component.text(" » ", Colors.DARK_GRAY)).append(Component.text(memberList.toString(), Colors.GRAY));

        player.sendMessage(TextUtils.line(31).append(Component.text(" Party Info ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(31)));
        player.sendMessage(Component.text(" * " + leader, Colors.RED, TextDecoration.BOLD));
        player.sendMessage(Component.text(" * " + members, Colors.RED, TextDecoration.BOLD));
        player.sendMessage(TextUtils.line(79));
    }
}
