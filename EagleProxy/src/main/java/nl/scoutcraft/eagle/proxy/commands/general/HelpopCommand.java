package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.PlayerLocationInfo;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.UsageException;
import nl.scoutcraft.eagle.proxy.discord.DiscordChannel;
import nl.scoutcraft.eagle.proxy.discord.DiscordEmbeds;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class HelpopCommand implements ICommand, IPlayerCommandExecutor {

    private final EagleProxy plugin = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("helpop").permission(Perms.HELPOP).executor(this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws UsageException {
        if (!context.hasRemaining())
            throw new UsageException("/helpop <message>");

        LocalDateTime timestamp = LocalDateTime.now();
        UUID id = UUID.randomUUID();
        String message = TextUtils.toString(CommandUtils.buildMessage(context.getRemaining(), 0, false));
        String playerName = player.getUsername();
        String serverName = player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse("Unknown");

        this.plugin.getSQLManager().addHelpop(id, player.getUniqueId(), message, timestamp, serverName);
        this.plugin.getPlayerManager().getPlayerChannel()
                .<PlayerLocationInfo>request()
                .onResponse(loc -> {
                    this.plugin.getSQLManager().addLocationToHelpop(id, loc);
                    this.sendDiscordMessage(id, playerName, serverName, message, timestamp, loc);
                })
                .onTimeout(() -> this.sendDiscordMessage(id, playerName, serverName, message, timestamp, null))
                .setTarget(player)
                .getLocation(player.getUniqueId());
        CommandMessages.HELPOP_SENT.send(player);

        IPlaceholder[] placeholders = new IPlaceholder[]{new Placeholder("%player%", player.getUsername()), new Placeholder("%server%", serverName)};
        EagleProxy.getProxy().getAllPlayers().stream().filter(p -> p.hasPermission(Perms.HELPOPS_ANNOUNCEMENT)).forEach(p -> CommandMessages.HELPOPS_NEW.send(p, placeholders));
    }

    private void sendDiscordMessage(UUID id, String playerName, String serverName, String message, LocalDateTime timestamp, @Nullable PlayerLocationInfo location) {
        String world = location == null ? null : location.getWorld();
        Integer x = location == null ? null : location.getX();
        Integer y = location == null ? null : location.getY();
        Integer z = location == null ? null : location.getZ();

        this.plugin.getDiscordManager().sendChannelMessage(DiscordChannel.HELPOPS, DiscordEmbeds.helpop(playerName, null, serverName, world, x, y, z, message, timestamp, false), messageId -> {
            this.plugin.getSQLManager().addDiscordMessageIdToHelpop(id, messageId);
        });
    }
}
