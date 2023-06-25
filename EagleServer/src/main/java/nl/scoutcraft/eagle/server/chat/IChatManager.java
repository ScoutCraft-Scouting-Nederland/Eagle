package nl.scoutcraft.eagle.server.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

public interface IChatManager extends Listener {

    void requestChatMessage(Player player, Consumer<String> action);
}
