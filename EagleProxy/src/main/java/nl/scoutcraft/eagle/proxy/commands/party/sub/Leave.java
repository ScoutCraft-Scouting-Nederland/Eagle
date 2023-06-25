package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Leave extends PartySubCommand {

    public Leave(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_LEAVE, "", Perms.PARTY_LEAVE, "leave");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Party party = super.partyManager.getParty(player);
        if (party == null) {
            PartyMessages.NOT_IN_PARTY.send(player);
            return;
        }

        if (party.isLeader(player)) {
            if (!party.newLeader()) {
                PartyMessages.PARTY_DISSOLVED.send(player);
                if (!party.getMembers().isEmpty())
                    party.getMembers().forEach(PartyMessages.PARTY_DISSOLVED::send);
                super.partyManager.deleteParty(party);
                return;
            }
            if (!party.getMembers().isEmpty())
                party.getMembers().forEach(member -> {
                    PartyMessages.LEADER_LEFT.send(member, new Placeholder("%oldLeader%", player.getUsername()));
                    PartyMessages.NEW_LEADER.send(member, new Placeholder("%newLeader%", party.getLeader().getUsername()));
                });
            PartyMessages.LEFT_PARTY.send(player);
            PartyMessages.LEADER_LEFT.send(party.getLeader(), new Placeholder("%oldLeader%", player.getUsername()));
            PartyMessages.HAVE_PROMOTED.send(party.getLeader());
            return;
        }

        if (super.partyManager.removeMember(player, party)) {
            PartyMessages.LEFT_PARTY.send(player);
        } else {
            PartyMessages.COULDNT_LEAVE.send(player);
        }
    }
}
