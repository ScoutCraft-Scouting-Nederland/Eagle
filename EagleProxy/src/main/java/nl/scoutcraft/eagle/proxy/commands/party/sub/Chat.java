package nl.scoutcraft.eagle.proxy.commands.party.sub;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.chat.ChatChannels;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

public class Chat extends PartySubCommand {

    public Chat(PartyManager partyManager) {
        super(partyManager, PartyMessages.INFO_CHAT, "{message}", Perms.PARTY_CHAT, "chat");
    }

    @Override
    public void onCommand(Player player, String[] args) {
        Party party = super.partyManager.getParty(player);
        if (party == null) {
            PartyMessages.NOT_IN_PARTY.send(player);
        } else if (args.length == 0) {
            CommandMessages.GENERAL_ADD_MESSAGE.send(player);
        } else {
            ChatChannels.PARTY.sendCommand(player, TextUtils.toString(CommandUtils.buildMessage(args, 0, player.hasPermission(Perms.CHAT_COLORED))));
        }
    }
}
