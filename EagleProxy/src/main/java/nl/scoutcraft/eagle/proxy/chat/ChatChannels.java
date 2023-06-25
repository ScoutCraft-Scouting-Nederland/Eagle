package nl.scoutcraft.eagle.proxy.chat;

import com.google.common.collect.Lists;
import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.locale.GeneralMessages;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class ChatChannels {

    public static ChatChannel TEAM = new CharChatChannel(GeneralMessages.TEAMCHAT_FORMAT, GeneralMessages.TEAMCHAT_SUBMIT_MESSAGE, '@', Perms.CHAT_TEAM, Perms.CHAT_TEAM);
    public static ChatChannel PARTY = new PartyChatChannel(PartyMessages.CHAT_FORMAT, PartyMessages.SUBMIT_MESSAGE, '!');

    private static final List<ChatChannel> CHANNELS = Lists.newArrayList(TEAM, PARTY);

    @NotNull
    public static Optional<ChatChannel> getFromChatMessage(CommandSource sender, String message) {
        for (ChatChannel channel : CHANNELS)
            if (channel.matchesChat(sender, message))
                return Optional.of(channel);
        return Optional.empty();
    }

    private ChatChannels(){}
}
