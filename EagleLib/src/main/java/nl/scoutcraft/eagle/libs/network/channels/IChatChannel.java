package nl.scoutcraft.eagle.libs.network.channels;

import nl.scoutcraft.eagle.libs.network.ProxySide;

public interface IChatChannel {

    @ProxySide
    default String getChatFormat() { return null; }
}
