package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Create extends PartySubCommand {

    public Create(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_CREATE, "", Perms.PARTY_CREATE, "create");
    }

    public void onCommand(Player player, String[] args) {
        if (super.partyManager.getParty(player) != null) {
            PartyMessages.ALREADY_IN_PARTY.send(player);
            return;
        }

        super.partyManager.createParty(player);
        PartyMessages.CREATED_PARTY.send(player);
    }
}
