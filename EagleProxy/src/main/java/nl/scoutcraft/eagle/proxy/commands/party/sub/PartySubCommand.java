package nl.scoutcraft.eagle.proxy.commands.party.sub;

import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

public abstract class PartySubCommand extends nl.scoutcraft.eagle.proxy.commands.party.PartySubCommand {

    protected final PartyManager partyManager;

    public PartySubCommand(PartyManager partyManager, IMessage message, String usage, String permission, String... commands) {
        super(message, usage, permission, commands);

        this.partyManager = partyManager;
    }
}
