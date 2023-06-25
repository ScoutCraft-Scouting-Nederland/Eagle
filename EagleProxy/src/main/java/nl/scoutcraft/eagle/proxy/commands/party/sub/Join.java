package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;

import java.util.List;

public class Join extends PartySubCommand {

    public Join(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_JOIN, "{name}", Perms.PARTY_JOIN, "join");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        ScoutPlayer scoutPlayer = EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player);

        if (super.partyManager.getParty(player) != null) {
            PartyMessages.ALREADY_IN_PARTY.send(player);
            return;
        }

        List<Party> partyInvites = scoutPlayer.getPartyInvites();
        Party party;

        if (args.length == 0) {
            if (!partyInvites.isEmpty()) {
                party = partyInvites.get(partyInvites.size() - 1);
            } else {
                PartyMessages.NO_INVITES.send(player);
                return;
            }
        } else {
            Player toJoin = EagleProxy.getProxy().getPlayer(args[0]).orElse(null);
            if (toJoin == null) {
                PartyMessages.PLAYER_NOT_ONLINE.send(player);
                return;
            }
            party = super.partyManager.getParty(toJoin);
        }

        if (party == null) {
            PartyMessages.NO_PARTY.send(player);
            return;
        }

        if (super.partyManager.addMember(scoutPlayer, party)) {
            partyInvites.remove(party);
            PartyMessages.HAVE_JOINED.send(player, new Placeholder("%party%", party.getLeader().getUsername()));
            if (!party.getMembers().isEmpty())
                party.getMembers().stream().filter(members -> !members.equals(player)).forEach(member -> PartyMessages.HAS_JOINED.send(member, new Placeholder("%toJoin%", player.getUsername())));
            PartyMessages.HAS_JOINED.send(party.getLeader(), new Placeholder("%toJoin%", player.getUsername()));
            return;
        }

        PartyMessages.NOT_JOINED.send(player);
    }
}
