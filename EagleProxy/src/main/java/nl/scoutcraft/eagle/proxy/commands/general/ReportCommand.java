package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.UsageException;
import nl.scoutcraft.eagle.proxy.discord.DiscordChannel;
import nl.scoutcraft.eagle.proxy.discord.DiscordEmbeds;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReportCommand implements ICommand, IPlayerCommandExecutor {

    private static final String USAGE = "/report <player> <message>";

    private final EagleProxy plugin = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("report")
                .permission(Perms.REPORT)
                .usage(USAGE)
                .child(Args.player("target"), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        if (!context.hasRemaining())
            throw new UsageException(USAGE);

        Player target = context.<Player>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        if (target.hasPermission(Perms.REPORT_EXEMPT))
            throw new MessageException(CommandMessages.REPORT_EXEMPT, new Placeholder("%player%", target.getUsername()));

        LocalDateTime timestamp = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        String serverName = target.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Unknown");
        Component message = CommandUtils.buildMessage(context.getRemaining(), 0, false);

        this.plugin.getSQLManager().addReport(id, player.getUniqueId(), target.getUniqueId(), TextUtils.toString(message), timestamp, serverName);
        CommandMessages.REPORT_SENT.send(player, new Placeholder("%player%", target.getUsername()));

        IPlaceholder[] placeholders = new IPlaceholder[]{new Placeholder("%reporter%", player.getUsername()), new Placeholder("%reportee%", target.getUsername()), new Placeholder("%server%", serverName)};
        EagleProxy.getProxy().getAllPlayers().stream().filter(p -> p.hasPermission(Perms.REPORTS_ANNOUNCEMENT)).forEach(p -> CommandMessages.REPORTS_NEW.send(p, placeholders));

        this.sendDiscordMessage(id, player.getUsername(), target.getUsername(), serverName, TextUtils.toString(message), timestamp);
    }

    private void sendDiscordMessage(UUID id, String reporter, String reportee, String serverName, String message, LocalDateTime timestamp) {
        this.plugin.getDiscordManager().sendChannelMessage(DiscordChannel.REPORTS, DiscordEmbeds.report(reporter, reportee, null, serverName, message, timestamp, false), messageId -> {
            this.plugin.getSQLManager().addDiscordMessageIdToReport(id, messageId);
        });
    }
}
