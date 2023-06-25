package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.party.PartySubCommand;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;

public class Help extends PartySubCommand {

    public Help() {
        super(PartyMessages.INFO_HELP, "", Perms.PARTY_HELP, "help");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        EagleProxy.getInstance().getPartyManager().sendChatMenu(player);
    }
}
