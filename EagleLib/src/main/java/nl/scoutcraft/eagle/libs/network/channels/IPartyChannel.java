package nl.scoutcraft.eagle.libs.network.channels;

import nl.scoutcraft.eagle.libs.network.ProxySide;
import nl.scoutcraft.eagle.libs.network.ServerSide;
import nl.scoutcraft.eagle.libs.party.PartyInfo;
import nl.scoutcraft.eagle.libs.party.PartyInvitation;
import nl.scoutcraft.eagle.libs.party.PartyMemberInfo;

import java.util.List;
import java.util.UUID;

public interface IPartyChannel {

    @ProxySide
    default UUID getPartyId(UUID playerId) { return null; }

    @ProxySide
    default PartyInfo getPartyInfo(UUID playerId) { return null; }

    @ProxySide
    default List<PartyMemberInfo> getMembers(UUID playerId) { return null; }

    @ProxySide
    default List<PartyInvitation> getInvites(UUID playerId) { return null; }

    @ServerSide
    default void onPartyCreate(PartyInfo party) {}

    @ServerSide
    default void onPartyDisband(UUID partyId) {}

    @ServerSide
    default void openMenu(UUID playerId, PartyInfo party) {}
}
