package nl.scoutcraft.eagle.server.party;

import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.ICommandsChannel;
import nl.scoutcraft.eagle.libs.network.channels.IPartyChannel;
import nl.scoutcraft.eagle.libs.party.PartyInfo;
import nl.scoutcraft.eagle.server.EagleServer;
import nl.scoutcraft.eagle.server.network.ServerNetworkChannel;
import nl.scoutcraft.eagle.server.party.menu.IPartyMenu;
import nl.scoutcraft.eagle.server.party.menu.NoPartyMenu;
import nl.scoutcraft.eagle.server.party.menu.PartyMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class PartyManager implements IPartyManager, IPartyChannel, ICommandsChannel {

    private final NoPartyMenu noPartyMenu;
    private final NetworkChannel<IPartyChannel> partyChannel;
    private final NetworkChannel<ICommandsChannel> commandsChannel;

    public PartyManager(EagleServer plugin) {
        this.noPartyMenu = new NoPartyMenu(this);
        this.partyChannel = new ServerNetworkChannel<>("eagle:party", IPartyChannel.class, this, plugin);
        this.commandsChannel = new ServerNetworkChannel<>("eagle:commands", ICommandsChannel.class, this, plugin);
    }

    @Override
    public void sendPartyAction(String action, Player player) {
        this.commandsChannel.request().setTarget(player).executeChatMessageCommand(player.getUniqueId(), "party " + action + " %value%", "%value%");
    }

    @Override
    public void sendCommand(Player player, String command) {
        this.commandsChannel.request().setTarget(player).executeCommand(player.getUniqueId(), command);
    }

    public NetworkChannel<IPartyChannel> getPartyChannel() {
        return this.partyChannel;
    }

    public NoPartyMenu getNoPartyMenu() {
        return this.noPartyMenu;
    }

    @Override
    public void onPartyCreate(PartyInfo party) {
        Player player = Bukkit.getPlayer(party.getLeader());
        if (player != null)
            new PartyMenu(this, party).open(player);
    }

    @Override
    public void onPartyDisband(UUID partyId) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            InventoryHolder holder = p.getOpenInventory().getTopInventory().getHolder();
            if (holder instanceof IPartyMenu)
                ((IPartyMenu) holder).closeIfMatches(partyId);
        });
    }

    @Override
    public void openMenu(UUID playerId, PartyInfo party) {
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            if (party == null) {
                this.noPartyMenu.open(player);
            } else {
                new PartyMenu(this, party).open(player);
            }
        }
    }

    @Override
    public void executeCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
