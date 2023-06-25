package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Disband extends PartySubCommand {

    public Disband(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_DISBAND, "", Perms.PARTY_DISBAND, "disband");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Party party = super.partyManager.getParty(player);
        if (party == null) {
            PartyMessages.NOT_IN_PARTY.send(player);
            return;
        }

        if (!party.isLeader(player)) {
            PartyMessages.NOT_PARTY_LEADER.send(player);
            return;
        }

        PartyMessages.PARTY_DISSOLVED.send(player);
        super.partyManager.deleteParty(party);
    }
}
