package nl.scoutcraft.eagle.server.chat;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class EmptyChatManager implements IChatManager {

    @Override
    public void requestChatMessage(Player player, Consumer<String> action) {}
}
