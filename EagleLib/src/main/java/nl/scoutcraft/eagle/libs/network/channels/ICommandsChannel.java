package nl.scoutcraft.eagle.libs.network.channels;

import nl.scoutcraft.eagle.libs.network.ProxySide;
import nl.scoutcraft.eagle.libs.network.ServerSide;

import java.util.UUID;

public interface ICommandsChannel {

    @ProxySide
    default void executeChatMessageCommand(UUID playerId, String command, String placeholderKey) {}

    @ProxySide
    default void executeCommand(UUID playerId, String command) {}

    @ProxySide
    @ServerSide
    default void executeCommand(String command) {}
}
