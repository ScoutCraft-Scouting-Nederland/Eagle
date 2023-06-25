package nl.scoutcraft.eagle.proxy.party;

import com.google.common.collect.Lists;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;
import nl.scoutcraft.eagle.proxy.utils.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WarpTask extends Task {

    private final Party party;
    private final List<Player> players;
    private int index;
    private int send;

    public WarpTask(Party party) {
        super(100, 100, TimeUnit.MILLISECONDS);

        this.party = party;
        this.players = Lists.newArrayList(party.getMembers());
        this.index = 0;
        this.send = 0;
    }

    @Override
    public void run() {
        if (this.index >= this.players.size()) {
            PartyMessages.HAVE_WARPED.send(this.party.getLeader(), new Placeholder("%sent%", Integer.toString(this.send)), new Placeholder("%tried%", Integer.toString(this.players.size())));
            this.stop();
        } else {
            this.warpPlayer(this.players.get(this.index++));
        }
    }

    private void warpPlayer(Player player) {
        ScoutPlayer scoutPlayer = EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player);
        RegisteredServer leaderServer = this.party.getLeader().getCurrentServer().map(ServerConnection::getServer).orElse(null);

        if (scoutPlayer.isInGame() || leaderServer == null || leaderServer.equals(player.getCurrentServer().map(ServerConnection::getServer).orElse(null)))
            return;

        player.createConnectionRequest(leaderServer).fireAndForget();
        PartyMessages.BEEN_WARPED.send(player);
        this.send++;
    }
}
