package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Kick extends PartySubCommand {

    public Kick(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_KICK, "{name}", Perms.PARTY_KICK, "kick");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            PartyMessages.PLAYERS_NAME.send(player);
            return;
        }

        Party party = super.partyManager.getParty(player);
        if (party == null) {
            PartyMessages.NOT_IN_PARTY.send(player);
            return;
        }

        if (!party.isLeader(player)) {
            PartyMessages.NOT_PARTY_LEADER.send(player);
            return;
        }

        Player toKick = EagleProxy.getProxy().getPlayer(args[0]).orElse(null);
        if (toKick == null) {
            PartyMessages.PLAYER_NOT_ONLINE.send(player);
            return;
        }

        if (super.partyManager.kickMember(toKick, party)) {
            PartyMessages.YOU_KICKED.send(party.getLeader(), new Placeholder("%toKick%", toKick.getUsername()));
        } else {
            PartyMessages.COULDNT_KICK.send(player);
        }
    }
}
