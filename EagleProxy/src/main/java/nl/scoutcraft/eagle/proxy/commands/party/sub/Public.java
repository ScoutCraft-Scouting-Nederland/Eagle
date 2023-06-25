package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Public extends PartySubCommand {

    public Public(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_PUBLIC, "", Perms.PARTY_PUBLIC, "public");
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

        if (party.isPublicParty()) {
            PartyMessages.ALREADY_MODE.send(player, new Placeholder("%state%", "public"));
            return;
        }

        party.setPublicParty(true);
        PartyMessages.NOW_MODE.send(player, new Placeholder("%state%", "public"));
    }
}
