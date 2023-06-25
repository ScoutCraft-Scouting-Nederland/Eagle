package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import nl.scoutcraft.eagle.libs.player.ReportInfo;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static nl.scoutcraft.eagle.proxy.utils.TextUtils.HUMAN_FORMATTER;
import static nl.scoutcraft.eagle.proxy.utils.TextUtils.formatTimeAgo;

public class ReportsCommand implements ICommand {

    private final EagleProxy plugin = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("reports")
                .permission(Perms.REPORTS)
                .usage("/reports {list|accept|close}")
//                .executor("list")
                .child(ICommand.node(Args.path("list").optional()).permission(Perms.REPORTS_LIST).executor((IPlayerCommandExecutor) this::sendList))
                .child(ICommand.node("accept").permission(Perms.REPORTS_ACCEPT).usage("/reports accept <id>").child(Args.uuid("uuid"), (IPlayerCommandExecutor) this::accept))
                .child(ICommand.node("close").permission(Perms.REPORTS_CLOSE).usage("/reports close <id>").child(Args.uuid("uuid"), (IPlayerCommandExecutor) this::close));
    }

    private void sendList(Player player, CommandContext context) {
        List<ReportInfo> accepted = this.plugin.getSQLManager().getAssignedReports(player.getUniqueId());

        player.sendMessage(Component.text(' '));
        if (!accepted.isEmpty())
            player.sendMessage(TextUtils.text(TextUtils.line(29)).append(Component.text(" Your Reports ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(29)));

        accepted.forEach(rep -> player.sendMessage(CommandMessages.REPORTS_ACCEPTED.get(player, new Placeholder("%reporter%", rep.getReporterName()), new Placeholder("%reportee%", rep.getReporteeName()), new Placeholder("%message%", rep.getMessage()), new Placeholder("%time%", formatTimeAgo(player, rep.getReportedAt())))
                .hoverEvent(HoverEvent.showText(CommandMessages.REPORTS_ACCEPTED_HOVER.get(player, new Placeholder("%timestamp%", rep.getReportedAt().format(HUMAN_FORMATTER)))))
                .clickEvent(ClickEvent.runCommand("/reports close " + rep.getIdentifier().toString()))));

        player.sendMessage(TextUtils.text(TextUtils.line(29)).append(Component.text(" Open Reports ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(29)));

        this.plugin.getSQLManager().getUnassignedReports().forEach(rep -> player.sendMessage(CommandMessages.REPORTS_OPEN.get(player, new Placeholder("%reporter%", rep.getReporterName()), new Placeholder("%reportee%", rep.getReporteeName()), new Placeholder("%message%", rep.getMessage()), new Placeholder("%time%", formatTimeAgo(player, rep.getReportedAt())))
                .hoverEvent(HoverEvent.showText(CommandMessages.REPORTS_OPEN_HOVER.get(player, new Placeholder("%timestamp%", rep.getReportedAt().format(HUMAN_FORMATTER)))))
                .clickEvent(ClickEvent.runCommand("/reports accept " + rep.getIdentifier().toString()))));

        player.sendMessage(TextUtils.line(79));
    }

    private void accept(Player player, CommandContext context) throws CommandException {
        UUID id = context.<UUID>get("uuid").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_UUID, new Placeholder("%uuid%", context.getRaw("uuid"))));
        LocalDateTime timestamp = LocalDateTime.now();

        ReportInfo report = this.plugin.getSQLManager().getReport(id);
        if (report == null) {
            CommandMessages.GENERAL_INVALID_REPORT.send(player);
            return;
        }

        this.plugin.getSQLManager().acceptReport(id, player.getUniqueId(), timestamp);

        if (report.getDiscordMessageId() != null)
            this.plugin.getDiscordManager().editChannelMessage(DiscordChannel.REPORTS, report.getDiscordMessageId(), DiscordEmbeds.report(
                    report.getReporterName(), report.getReporteeName(), player.getUsername(), report.getServerName(), report.getMessage(), report.getReportedAt(), false));

        this.sendList(player, context);
    }

    private void close(Player player, CommandContext context) throws CommandException {
        UUID id = context.<UUID>get("uuid").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_UUID, new Placeholder("%uuid%", context.getRaw("uuid"))));
        LocalDateTime timestamp = LocalDateTime.now();

        ReportInfo report = this.plugin.getSQLManager().getReport(id);
        if (report == null) {
            CommandMessages.GENERAL_INVALID_REPORT.send(player);
            return;
        }

        this.plugin.getSQLManager().closeReport(id, timestamp);

        if (report.getDiscordMessageId() != null)
            this.plugin.getDiscordManager().editChannelMessage(DiscordChannel.REPORTS, report.getDiscordMessageId(), DiscordEmbeds.report(
                    report.getReporterName(), report.getReporteeName(), report.getHandledByName(), report.getServerName(), report.getMessage(), report.getReportedAt(), true));

        this.sendList(player, context);
    }
}
