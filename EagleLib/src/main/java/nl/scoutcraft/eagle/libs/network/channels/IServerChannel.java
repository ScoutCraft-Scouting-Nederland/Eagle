package nl.scoutcraft.eagle.libs.network.channels;

import nl.scoutcraft.eagle.libs.network.ProxySide;
import nl.scoutcraft.eagle.libs.network.ServerSide;

public interface IServerChannel {

    @ProxySide
    default void setGameState(String state) {}

    @ServerSide
    default void setGameState(String serverName, String state) {}

    @ServerSide
    default double[] getTPS() { return new double[]{0d,0d,0d}; }
}
