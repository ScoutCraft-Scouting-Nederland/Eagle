package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import nl.scoutcraft.eagle.libs.player.HelpopInfo;
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

public class HelpopsCommand implements ICommand {

    private final EagleProxy plugin = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("helpops")
                .permission(Perms.HELPOPS)
                .usage("/helpops {list|accept|close}")
                .child(ICommand.node(Args.path("list").optional()).permission(Perms.HELPOPS_LIST).executor((IPlayerCommandExecutor) this::sendList))
                .child(ICommand.node("accept").usage("/helpops accept <id>").permission(Perms.HELPOPS_ACCEPT).child(Args.uuid("uuid"), (IPlayerCommandExecutor) this::accept))
                .child(ICommand.node("close").usage("/helpops close <id>").permission(Perms.HELPOPS_CLOSE).child(Args.uuid("uuid"), (IPlayerCommandExecutor) this::close));
    }

    private void sendList(Player player, CommandContext context) {
        List<HelpopInfo> accepted = this.plugin.getSQLManager().getAssignedHelpops(player.getUniqueId());

        player.sendMessage(Component.empty());
        if (!accepted.isEmpty())
            player.sendMessage(TextUtils.text(TextUtils.line(29)).append(Component.text(" Your HelpOps ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(29)));

        accepted.forEach(helpop ->
                player.sendMessage(CommandMessages.HELPOPS_ACCEPTED.get(player, new Placeholder("%player%", helpop.getPlayerName()), new Placeholder("%server%", helpop.getServer()), new Placeholder("%message%", helpop.getMessage()), new Placeholder("%time%", formatTimeAgo(player, helpop.getRequestedAt())))
                .hoverEvent(HoverEvent.showText(CommandMessages.HELPOPS_ACCEPTED_HOVER.get(player, new Placeholder("%location%", helpop.getLocation()), new Placeholder("%timestamp%", helpop.getRequestedAt().format(HUMAN_FORMATTER)))))
                .clickEvent(ClickEvent.runCommand("/helpops close " + helpop.getIdentifier().toString()))));

        player.sendMessage(TextUtils.text(TextUtils.line(29)).append(Component.text(" Open HelpOps ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(29)));

        this.plugin.getSQLManager().getUnassignedHelpops().forEach(helpop ->
                player.sendMessage(CommandMessages.HELPOPS_OPEN.get(player, new Placeholder("%player%", helpop.getPlayerName()), new Placeholder("%server%", helpop.getServer()), new Placeholder("%message%", helpop.getMessage()), new Placeholder("%time%", formatTimeAgo(player, helpop.getRequestedAt())))
                .hoverEvent(HoverEvent.showText(CommandMessages.HELPOPS_OPEN_HOVER.get(player, new Placeholder("%location%", helpop.getLocation()), new Placeholder("%timestamp%", helpop.getRequestedAt().format(HUMAN_FORMATTER)))))
                .clickEvent(ClickEvent.runCommand("/helpops accept " + helpop.getIdentifier().toString()))));

        player.sendMessage(TextUtils.line(79));
    }

    private void accept(Player player, CommandContext context) throws CommandException {
        UUID id = context.<UUID>get("uuid").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_UUID, new Placeholder("%uuid%", context.getRaw("uuid"))));
        LocalDateTime timestamp = LocalDateTime.now();

        HelpopInfo helpop = this.plugin.getSQLManager().getHelpop(id);
        if (helpop == null) {
            CommandMessages.GENERAL_INVALID_HELPOP.send(player);
            return;
        }

        this.plugin.getSQLManager().acceptHelpop(id, player.getUniqueId(), timestamp);

        if (helpop.getDiscordMessageId() != null)
            this.plugin.getDiscordManager().editChannelMessage(DiscordChannel.HELPOPS, helpop.getDiscordMessageId(), DiscordEmbeds.helpop(
                    helpop.getPlayerName(), player.getUsername(), helpop.getServer(), helpop.getWorld(),
                    helpop.getX(), helpop.getY(), helpop.getZ(),
                    helpop.getMessage(), helpop.getRequestedAt(), false));

        this.sendList(player, context);
    }

    private void close(Player player, CommandContext context) throws CommandException {
        UUID id = context.<UUID>get("uuid").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_UUID, new Placeholder("%uuid%", context.getRaw("uuid"))));
        LocalDateTime timestamp = LocalDateTime.now();

        HelpopInfo helpop = this.plugin.getSQLManager().getHelpop(id);
        if (helpop == null) {
            CommandMessages.GENERAL_INVALID_HELPOP.send(player);
            return;
        }

        this.plugin.getSQLManager().closeHelpop(id, timestamp);

        if (helpop.getDiscordMessageId() != null)
            this.plugin.getDiscordManager().editChannelMessage(DiscordChannel.HELPOPS, helpop.getDiscordMessageId(), DiscordEmbeds.helpop(
                    helpop.getPlayerName(), helpop.getHandledByName(), helpop.getServer(), helpop.getWorld(),
                    helpop.getX(), helpop.getY(), helpop.getZ(),
                    helpop.getMessage(), helpop.getRequestedAt(), true));

        this.sendList(player, context);
    }
}
