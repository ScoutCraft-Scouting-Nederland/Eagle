package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public class Invite extends PartySubCommand {

    public Invite(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_INVITE, "{name}", Perms.PARTY_INVITE, "invite");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            PartyMessages.PLAYERS_NAME.send(player);
            return;
        }

        Party party = super.partyManager.getParty(player);
        if (party == null) {
            party = super.partyManager.createParty(player);
            PartyMessages.CREATED_PARTY.send(player);
        }

        if (!party.isLeader(player)) {
            PartyMessages.NOT_PARTY_LEADER.send(player);
            return;
        }

        Player toInvite = EagleProxy.getProxy().getPlayer(args[0]).orElse(null);

        if (toInvite == null) {
            PartyMessages.PLAYER_NOT_ONLINE.send(player);
            return;
        }

        if (EagleProxy.getInstance().getPlayerManager().getScoutPlayer(toInvite).getPartyInvites().contains(party)) {
            PartyMessages.ALREADY_INVITED.send(player, new Placeholder("%toInvite%", toInvite.getUsername()));
            return;
        }

        if (party.getLeader().equals(toInvite)) {
            PartyMessages.INVITE_SELF.send(player);
            return;
        }

        if (party.getMembers().contains(toInvite)) {
            PartyMessages.ALREADY_MEMBER.send(player, new Placeholder("%toInvite%", toInvite.getUsername()));
            return;
        }

        party.inviteMember(toInvite);
        PartyMessages.INVITED.send(player, new Placeholder("%toInvite%", toInvite.getUsername()));
    }
}
