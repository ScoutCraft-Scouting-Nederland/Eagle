package nl.scoutcraft.eagle.proxy.chat;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

import java.util.function.Predicate;

public abstract class ChatChannel {

    protected final IMessage format;
    protected final IMessage lengthWarning;

    public ChatChannel(IMessage format, IMessage lengthWarning) {
        this.format = format;
        this.lengthWarning = lengthWarning;
    }

    public abstract boolean matchesChat(CommandSource sender, String message);
    public abstract boolean matchesCommand(CommandSource sender);

    public abstract void sendChat(CommandSource sender, String message);
    public abstract void sendCommand(CommandSource sender, String message);

    protected boolean validateMessageLength(CommandSource sender, String message) {
        if (message.length() < 2) {
            this.lengthWarning.send(sender);
            return false;
        }
        return true;
    }

    protected void send(CommandSource sender, String message, Predicate<Player> filter) {
        if (message.length() < 2) {
            this.lengthWarning.send(sender);
        } else {
            IPlaceholder[] placeholders = new Placeholder[]{new Placeholder("%player%", sender instanceof Player ? ((Player) sender).getUsername() : "Console"), new Placeholder("%message%", message)};
            EagleProxy.getProxy().getAllPlayers().stream().filter(filter).forEach(p -> this.format.send(p, placeholders));
        }
    }
}
