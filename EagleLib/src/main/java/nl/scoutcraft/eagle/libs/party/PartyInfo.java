package nl.scoutcraft.eagle.libs.party;

import java.util.List;
import java.util.UUID;

public class PartyInfo {

    private final UUID partyId;
    private final boolean publicParty;

    private final UUID leader;
    private final List<UUID> members;

    public PartyInfo(UUID partyId, boolean publicParty, UUID leader, List<UUID> members) {
        this.partyId = partyId;
        this.publicParty = publicParty;
        this.leader = leader;
        this.members = members;
    }

    public UUID getPartyId() {
        return this.partyId;
    }

    public boolean isPublicParty() {
        return this.publicParty;
    }

    public UUID getLeader() {
        return this.leader;
    }

    public List<UUID> getMembers() {
        return this.members;
    }
}
