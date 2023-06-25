package nl.scoutcraft.eagle.server.party;

import org.bukkit.entity.Player;

public interface IPartyManager {

    void sendPartyAction(String action, Player player);
    void sendCommand(Player player, String command);
}
