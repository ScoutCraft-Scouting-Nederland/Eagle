package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import nl.scoutcraft.eagle.libs.player.BugInfo;
import nl.scoutcraft.eagle.libs.utils.Colors;
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
import nl.scoutcraft.eagle.proxy.discord.DiscordChannel;
import nl.scoutcraft.eagle.proxy.discord.DiscordEmbeds;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static nl.scoutcraft.eagle.proxy.utils.TextUtils.HUMAN_FORMATTER;
import static nl.scoutcraft.eagle.proxy.utils.TextUtils.formatTimeAgo;

public class BugsCommand implements ICommand {

    private final EagleProxy plugin = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("bugs")
                .permission(Perms.BUGS)
                .usage("/bugs {list|accept|close}")
//                .executor("list")
                .child(ICommand.node(Args.path("list").optional()).permission(Perms.BUGS_LIST).executor((IPlayerCommandExecutor) this::sendList))
                .child(ICommand.node("accept").permission(Perms.BUGS_ACCEPT).usage("/bugs accept <id>").child(Args.uuid("uuid"), (IPlayerCommandExecutor) this::accept))
                .child(ICommand.node("close").permission(Perms.BUGS_CLOSE).usage("/bugs close <id>").child(Args.uuid("uuid"), (IPlayerCommandExecutor) this::close));
    }

    private void sendList(Player player, CommandContext context) {
        List<BugInfo> accepted = this.plugin.getSQLManager().getAssignedBugs(player.getUniqueId());

        player.sendMessage(Component.text(' '));
        if (!accepted.isEmpty())
            player.sendMessage(TextUtils.text(TextUtils.line(31)).append(Component.text(" Your Bugs ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(31)));

        accepted.forEach(bug -> player.sendMessage(CommandMessages.BUGS_ACCEPTED.get(player, new Placeholder("%reporter%", bug.getReporterName()), new Placeholder("%server%", bug.getServerName()), new Placeholder("%message%", bug.getMessage()), new Placeholder("%time%", formatTimeAgo(player, bug.getReportedAt())))
                .hoverEvent(HoverEvent.showText(CommandMessages.BUGS_ACCEPTED_HOVER.get(player, new Placeholder("%timestamp%", bug.getReportedAt().format(HUMAN_FORMATTER)))))
                .clickEvent(ClickEvent.runCommand("/bugs close " + bug.getIdentifier().toString()))));

        player.sendMessage(TextUtils.text(TextUtils.line(31)).append(Component.text(" Open Bugs ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(31)));

        this.plugin.getSQLManager().getUnassignedBugs().forEach(bug -> player.sendMessage(CommandMessages.BUGS_OPEN.get(player, new Placeholder("%reporter%", bug.getReporterName()), new Placeholder("%server%", bug.getServerName()), new Placeholder("%message%", bug.getMessage()), new Placeholder("%time%", formatTimeAgo(player, bug.getReportedAt())))
                .hoverEvent(HoverEvent.showText(CommandMessages.BUGS_OPEN_HOVER.get(player, new Placeholder("%timestamp%", bug.getReportedAt().format(HUMAN_FORMATTER)))))
                .clickEvent(ClickEvent.runCommand("/bugs accept " + bug.getIdentifier().toString()))));

        player.sendMessage(TextUtils.line(79));
    }

    private void accept(Player player, CommandContext context) throws CommandException {
        UUID id = context.<UUID>get("uuid").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_UUID, new Placeholder("%uuid%", context.getRaw("uuid"))));
        LocalDateTime timestamp = LocalDateTime.now();

        BugInfo bug = this.plugin.getSQLManager().getBug(id);
        if (bug == null) {
            CommandMessages.GENERAL_INVALID_BUG.send(player);
            return;
        }

        this.plugin.getSQLManager().acceptBug(id, player.getUniqueId(), timestamp);

        if (bug.getDiscordMessageId() != null)
            this.plugin.getDiscordManager().editChannelMessage(DiscordChannel.BUGS, bug.getDiscordMessageId(), DiscordEmbeds.bug(
                    bug.getReporterName(), player.getUsername(), bug.getServerName(), bug.getMessage(), bug.getResolvedMessage(), bug.getReportedAt(), false));

        this.sendList(player, context);
    }

    private void close(Player player, CommandContext context) throws CommandException {
        UUID id = context.<UUID>get("uuid").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_UUID, new Placeholder("%uuid%", context.getRaw("uuid"))));

        BugInfo bug = this.plugin.getSQLManager().getBug(id);
        if (bug == null) {
            CommandMessages.GENERAL_INVALID_BUG.send(player);
            return;
        }

        CommandMessages.BUGS_ADD_MESSAGE.send(player);
        this.plugin.getChatManager().requestChatMessage(player.getUniqueId(), (p, msg) -> {
            if (msg.charAt(0) == 'y') {
                CommandMessages.BUGS_TYPE_MESSAGE.send(player);
                this.plugin.getChatManager().requestChatMessage(p.getUniqueId(), (p2, msg2) -> this.close(p2, bug, msg2));
            } else {
                this.close(p, bug, null);
            }
        });
    }

    private void close(Player player, BugInfo bug, @Nullable String message) {
        LocalDateTime timestamp = LocalDateTime.now();

        this.plugin.getSQLManager().closeBug(bug.getIdentifier(), message, timestamp);

        if (bug.getDiscordMessageId() != null)
            this.plugin.getDiscordManager().editChannelMessage(DiscordChannel.BUGS, bug.getDiscordMessageId(), DiscordEmbeds.bug(
                    bug.getReporterName(), bug.getHandledByName(), bug.getServerName(), bug.getMessage(), message, bug.getReportedAt(), true));

        this.sendList(player, new CommandContext(new String[0]));
    }
}
