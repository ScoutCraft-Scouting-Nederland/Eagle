package nl.scoutcraft.eagle.proxy.chat;

import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import org.jetbrains.annotations.Nullable;

public class CharChatChannel extends ChatChannel {

    private final char startChar;
    @Nullable private final String senderPermission;
    private final String receiverPermission;

    public CharChatChannel(IMessage format, IMessage lengthWarning, char startChar, @Nullable String senderPermission, String receiverPermission) {
        super(format, lengthWarning);

        this.startChar = startChar;
        this.senderPermission = senderPermission;
        this.receiverPermission = receiverPermission;
    }

    @Override
    public boolean matchesChat(CommandSource sender, String message) {
        return message.charAt(0) == startChar && this.matchesCommand(sender);
    }

    @Override
    public boolean matchesCommand(CommandSource sender) {
        return this.senderPermission == null || sender.hasPermission(this.senderPermission);
    }

    @Override
    public void sendChat(CommandSource sender, String message) {
        this.sendCommand(sender, message.substring(1));
    }

    @Override
    public void sendCommand(CommandSource sender, String message) {
        super.send(sender, message, p -> p.hasPermission(this.receiverPermission));
    }
}
