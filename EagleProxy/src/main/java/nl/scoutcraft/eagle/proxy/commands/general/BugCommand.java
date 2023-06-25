package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.UsageException;
import nl.scoutcraft.eagle.proxy.discord.DiscordChannel;
import nl.scoutcraft.eagle.proxy.discord.DiscordEmbeds;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class BugCommand implements ICommand, IPlayerCommandExecutor {

    private final EagleProxy plugin = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("bug").permission(Perms.BUG).executor(this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        if (!context.hasRemaining())
            throw new UsageException("/bug <message>");

        LocalDateTime timestamp = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        String serverName = player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Unknown");
        String message = TextUtils.toString(CommandUtils.buildMessage(context.getRemaining(), 0, false));

        this.plugin.getSQLManager().addBug(id, player.getUniqueId(), message, timestamp, serverName);
        CommandMessages.BUG_SENT.send(player);

        IPlaceholder[] placeholders = new IPlaceholder[]{new Placeholder("%reporter%", player.getUsername()), new Placeholder("%server%", serverName)};
        EagleProxy.getProxy().getAllPlayers().stream().filter(p -> p.hasPermission(Perms.BUGS_ANNOUNCEMENT)).forEach(p -> CommandMessages.BUGS_NEW.send(p, placeholders));

        this.sendDiscordMessage(id, player.getUsername(),  serverName, message, timestamp);
    }

    private void sendDiscordMessage(UUID id, String reporter, String serverName, String message, LocalDateTime timestamp) {
        this.plugin.getDiscordManager().sendChannelMessage(DiscordChannel.BUGS, DiscordEmbeds.bug(reporter, null, serverName, message, null, timestamp, false), messageId -> {
            this.plugin.getSQLManager().addDiscordMessageIdToBug(id, messageId);
        });
    }
}
