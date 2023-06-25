package nl.scoutcraft.eagle.scotty.discord;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

public class DiscordListener extends ListenerAdapter {

    private final DiscordManager manager;

    private final List<IDiscordRegisterChecker> checkers;

    public DiscordListener(DiscordManager manager) {
        this.manager = manager;
        this.checkers = new ArrayList<>();
    }

    public void registerChecker(IDiscordRegisterChecker checker) {
        this.checkers.add(checker);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot() || event.isFromType(ChannelType.PRIVATE))
            return;

        Message message = event.getMessage();
        if (!message.getContentRaw().equalsIgnoreCase("!register"))
            return;

        try {
            message.delete().queue();
        } catch (Exception exc) {
        }

        String errorMessage = this.checkers.stream().map(c -> c.checkRegister(user.getId())).filter(Objects::nonNull).findFirst().orElse(null);
        if (errorMessage != null) {
            user.openPrivateChannel().queue(channel -> channel.sendMessage(errorMessage).queue());
            return;
        }

        final String token = token();
        user.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(DiscordEmbedUtil.register(token)).queue(response -> this.manager.register(token, user, response.getId())));
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot() || !event.getMessage().getContentRaw().equalsIgnoreCase("!register"))
            return;

        String errorMessage = this.checkers.stream().map(c -> c.checkRegister(user.getId())).filter(Objects::nonNull).findFirst().orElse(null);
        if (errorMessage != null) {
            user.openPrivateChannel().queue(channel -> channel.sendMessage(errorMessage).queue());
            return;
        }

        final String token = token();
        event.getChannel().sendMessageEmbeds(DiscordEmbedUtil.register(token)).queue(response -> this.manager.register(token, user, response.getId()));
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.manager.onReady();
    }

    private String token() {
        StringBuilder token = new StringBuilder();
        Random r = new Random();

        IntStream.range(0, 6).map(i -> r.nextInt(36)).forEach(val -> {
            if (val >= 26) token.append(val - 26);
            else token.append((char) (97 + val));
        });

        return token.toString();
    }
}
