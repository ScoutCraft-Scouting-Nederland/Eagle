package nl.scoutcraft.eagle.server.party;

import org.bukkit.entity.Player;

public class EmptyPartyManager implements IPartyManager {

    @Override
    public void sendPartyAction(String action, Player player) {}

    @Override
    public void sendCommand(Player player, String command) {}
}
