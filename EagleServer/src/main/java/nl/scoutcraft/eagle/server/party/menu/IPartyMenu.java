package nl.scoutcraft.eagle.server.party.menu;

import nl.scoutcraft.eagle.server.gui.inventory.base.IInventoryMenu;

import java.util.UUID;

public interface IPartyMenu extends IInventoryMenu {

    UUID getPartyId();

    default void closeIfMatches(UUID partyId) {
        if (partyId.equals(this.getPartyId()))
            this.close();
    }
}
