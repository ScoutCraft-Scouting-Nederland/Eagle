package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Teleport extends PartySubCommand {

    public Teleport(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_TELEPORT, "", Perms.PARTY_TELEPORT, "teleport", "tp");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Party party = super.partyManager.getParty(player);
        if (party == null) {
            PartyMessages.NOT_IN_PARTY.send(player);
            return;
        }

        if (party.isLeader(player)) {
            PartyMessages.SELF_TELEPORT.send(player);
            return;
        }

        if (party.teleport(player)) {
            PartyMessages.HAS_TELEPORTED.send(party.getLeader(), new Placeholder("%member%", player.getUsername()));
            PartyMessages.HAVE_TELEPORTED.send(player);
            return;
        }

        PartyMessages.ALREADY_IN_SERVER.send(player);
    }
}
