package nl.scoutcraft.eagle.proxy.party;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Party {

    private final UUID uuid;

    private Player leader;

    private final List<Player> members;

    private boolean publicParty;

    public Party(Player leader) {
        this.uuid = UUID.randomUUID();
        this.leader = leader;
        this.members = new ArrayList<>();
        this.publicParty = false;
    }

    public UUID getId() {
        return this.uuid;
    }

    public boolean isLeader(Player player) {
        return this.leader == player;
    }

    public boolean isLeader(UUID uuid) {
        return this.leader.getUniqueId().equals(uuid);
    }

    public Player getLeader() {
        return this.leader;
    }

    public boolean setLeader(Player player) {
        if (!this.members.contains(player)) return false;

        this.members.remove(player);
        this.members.add(this.leader);
        this.leader = player;
        return true;
    }

    public boolean newLeader() {
        if (this.members.isEmpty()) return false;

        this.leader = this.members.remove(new Random().nextInt(this.members.size()));
        return true;
    }

    public List<Player> getMembers() {
        return this.members;
    }

    public void inviteMember(Player player) {
        ScoutPlayer scoutPlayer = EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player);
        scoutPlayer.getPartyInvites().add(this);

        PartyMessages.ARE_INVITED.send(player, new Placeholder("%leader%", this.leader.getUsername()));
        PartyMessages.JOIN_PARTY.send(player, new Placeholder("%leader%", this.leader.getUsername()));

        EagleProxy.getProxy().getScheduler().buildTask(EagleProxy.getInstance(), () -> this.removeInvitation(scoutPlayer)).delay(30, TimeUnit.SECONDS).schedule();
    }

    private void removeInvitation(ScoutPlayer scoutplayer) {
        if (!scoutplayer.getPartyInvites().remove(this)) {
            return;
        }

        Optional<Player> player = scoutplayer.getPlayer();
        PartyMessages.P_INVITE_EXPIRED.send(this.leader, new Placeholder("%invitee%", player.map(Player::getUsername).orElse(scoutplayer.getDisplayName().orElse("Unknown"))));
        player.ifPresent(p -> PartyMessages.INVITE_EXPIRED.send(p, new Placeholder("%party%", this.leader.getUsername())));
    }

    protected boolean addMember(ScoutPlayer scoutplayer) {
        Player player = scoutplayer.getPlayer().orElse(null);
        if (player == null)
            return false;

        if (this.members.contains(player)) return false;

        if (isPublicParty() || scoutplayer.getPartyInvites().contains(this)) {
            this.members.add(player);
            scoutplayer.getPartyInvites().remove(this);
            return true;
        }

        return false;
    }

    protected boolean removeMember(Player player) {
        if (!this.members.remove(player))
            return false;

        PartyMessages.PUB_LEFT_PARTY.send(this.leader, new Placeholder("%member%", player.getUsername()));
        if (!this.members.isEmpty())
            this.members.forEach(member -> PartyMessages.PUB_LEFT_PARTY.send(member, new Placeholder("%member%", player.getUsername())));
        return true;
    }

    public boolean removeMember(UUID uuid, String username) {
        if (!this.members.removeIf(p -> p.getUniqueId().equals(uuid)))
            return false;

        PartyMessages.PUB_LEFT_PARTY.send(this.leader, new Placeholder("%member%", username));
        if (!this.members.isEmpty())
            this.members.forEach(member -> PartyMessages.PUB_LEFT_PARTY.send(member, new Placeholder("%member%", username)));
        return true;
    }

    protected boolean kickMember(Player player) {
        if (!this.members.remove(player))
            return false;

        PartyMessages.WERE_KICKED.send(player);
        if (!this.members.isEmpty())
            this.members.forEach(member -> PartyMessages.HAS_KICKED.send(member, new Placeholder("%toKick%", player.getUsername())));
        return true;
    }

    public boolean isInParty(Player player) {
        return this.isLeader(player) || this.members.contains(player);
    }

    public void partyJump() {
        new WarpTask(this).start();
    }

    public boolean teleport(Player player) {
        RegisteredServer server = this.leader.getCurrentServer().map(ServerConnection::getServer).orElse(null);
        if (server == null)
            return false;

        if (!player.getCurrentServer().isPresent() || player.getCurrentServer().get().getServer().equals(server))
            return false;

        player.createConnectionRequest(server).fireAndForget();
        return true;
    }

    public boolean isPublicParty() {
        return this.publicParty;
    }

    public void setPublicParty(boolean state) {
        this.publicParty = state;
    }
}
