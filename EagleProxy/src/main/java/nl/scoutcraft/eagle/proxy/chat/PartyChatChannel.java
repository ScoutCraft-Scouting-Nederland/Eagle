package nl.scoutcraft.eagle.proxy.chat;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.format.TextColor;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;

import java.util.Iterator;

public class PartyChatChannel extends ChatChannel {

    private final PartyManager manager = EagleProxy.getInstance().getPartyManager();
    private final char startChar;

    public PartyChatChannel(IMessage format, IMessage lengthWarning, char startChar) {
        super(format, lengthWarning);

        this.startChar = startChar;
    }

    @Override
    public boolean matchesChat(CommandSource sender, String message) {
        return message.charAt(0) == this.startChar && this.matchesCommand(sender);
    }

    @Override
    public boolean matchesCommand(CommandSource sender) {
        return sender instanceof Player && this.manager.getParty((Player) sender) != null;
    }

    @Override
    public void sendChat(CommandSource sender, String message) {
        if (message.charAt(0) == this.startChar)
            this.sendCommand(sender, message.substring(1));
    }

    @Override
    public void sendCommand(CommandSource sender, String message) {
        Player player;
        Party party;
        if (!(sender instanceof Player) || (party = this.manager.getParty(player = (Player) sender)) == null || !super.validateMessageLength(sender, message))
            return;

        TextColor color = (party.isLeader(player) ? Colors.RED : Colors.GRAY);
        IPlaceholder[] placeholders = new IPlaceholder[]{new Placeholder("%player%", color + player.getUsername()), new Placeholder("%message%", message)};

        Iterator<Player> members = party.getMembers().iterator();
        Player target = party.getLeader();

        do {
            super.format.send(target, placeholders);
        } while ((target = members.hasNext() ? members.next() : null) != null);
    }
}
