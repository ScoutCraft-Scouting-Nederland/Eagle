package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Transfer extends PartySubCommand {

    public Transfer(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_TRANSFER, "{name}", Perms.PARTY_TRANSFER, "transfer");
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

        Player toLeader = EagleProxy.getProxy().getPlayer(args[0]).orElse(null);

        if (toLeader == null) {
            PartyMessages.PLAYER_NOT_ONLINE.send(player);
            return;
        }

        if (party.setLeader(toLeader)) {
            PartyMessages.PROMOTED.send(player, new Placeholder("%newLeader%", toLeader.getUsername()));
            PartyMessages.HAVE_PROMOTED.send(toLeader);
            party.getMembers().forEach(member -> PartyMessages.PUB_PROMOTED.send(member, new Placeholder("%newLeader%", toLeader.getUsername())));
            return;
        }

        PartyMessages.NOT_MEMBER.send(player, new Placeholder("%newLeader%", toLeader.getUsername()));
    }
}
