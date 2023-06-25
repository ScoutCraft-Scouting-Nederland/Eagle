package nl.scoutcraft.eagle.proxy.commands.message;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.ComponentPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.IgnoreList;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BaseMessageCommand {

    protected static final UUID CONSOLE_UUID = UUID.randomUUID();
    protected static UUID CONSOLE_REPLY_TARGET = null;

    protected final EagleProxy plugin = EagleProxy.getInstance();

    public void sendMessage(CommandSource sender, CommandSource receiver, Component message) {
        Player senderPlayer = sender instanceof Player ? (Player) sender : null;
        Player receiverPlayer = receiver instanceof Player ? (Player) receiver : null;

        String senderName = senderPlayer != null ? senderPlayer.getUsername() : "Console";
        String receiverName = receiverPlayer != null ? receiverPlayer.getUsername() : "Console";

        UUID senderUuid = senderPlayer != null ? senderPlayer.getUniqueId() : CONSOLE_UUID;
        UUID receiverUuid = receiverPlayer != null ? receiverPlayer.getUniqueId() : CONSOLE_UUID;

        IPlaceholder[] placeholders = new IPlaceholder[]{new Placeholder("%sender%", senderName), new Placeholder("%receiver%", receiverName), new ComponentPlaceholder("%message%", message)};

        IgnoreList ignores = this.plugin.getChatManager().getIgnores();
        if (ignores.isIgnoring(receiverUuid, senderUuid)) {
            CommandMessages.MESSAGE_IGNORED.send(sender, placeholders);
            return;
        }
        if (ignores.isIgnoring(senderUuid, receiverUuid)) {
            CommandMessages.MESSAGE_YOU_IGNORED.send(sender, placeholders);
            return;
        }

        CommandMessages.MESSAGE_FORMAT.send(sender, placeholders);
        CommandMessages.MESSAGE_FORMAT.send(receiver, placeholders);

        this.setReplyTarget(senderPlayer, receiverUuid);
        this.setReplyTarget(receiverPlayer, senderUuid);

        // SocialSpy
        EagleProxy.getProxy().getAllPlayers().stream()
                .filter(p -> p != senderPlayer && p != receiverPlayer && this.plugin.getPlayerManager().getScoutPlayer(p).isSpying())
                .forEach(p -> CommandMessages.SOCIALSPY_FORMAT.send(p, placeholders));
    }

    public void setReplyTarget(@Nullable Player player, UUID uuid) {
        if (player == null) CONSOLE_REPLY_TARGET = uuid;
        else this.plugin.getPlayerManager().getScoutPlayer(player).setReplyTarget(uuid);
    }
}
