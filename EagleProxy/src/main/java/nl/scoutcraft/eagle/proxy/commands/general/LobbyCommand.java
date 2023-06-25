package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.server.LoadBalancer;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;

public class LobbyCommand implements ICommand, IPlayerCommandExecutor {

    private final EagleProxy plugin;

    public LobbyCommand(EagleProxy plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandBuilder build() {
        return ICommand.builder("lobby", "hub").permission(Perms.LOBBY).executor(this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws MessageException {
        LoadBalancer balancer = this.plugin.getServerManager().getLoadBalancer();

        if (player.getCurrentServer().map(s -> balancer.isLobby(s.getServer())).orElse(true))
            throw new MessageException(CommandMessages.LOBBY_ALREADY_IN);

        ScoutServer server = balancer.getLeastActive().orElseThrow(() -> new MessageException(CommandMessages.LOBBY_NOT_FOUND));

        player.createConnectionRequest(server.getServer()).fireAndForget();
    }
}
