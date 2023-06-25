package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.TextUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class SeenCommand implements ICommand, ICommandExecutor {

    private final EagleProxy plugin;

    public SeenCommand(EagleProxy plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandBuilder build() {
        return ICommand.builder("seen")
                .permission(Perms.SEEN)
                .usage("/seen <player>")
                .child(Args.offlinePlayer("target"), this);
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws MessageException {
        PlayerInfo info = context.<PlayerInfo>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        Optional<Player> found = EagleProxy.getProxy().getPlayer(info.getUniqueId());

        if (found.isPresent()) {
            this.sendOnlineSeenInfo(sender, found.get());
        } else {
            this.sendOfflineSeenInfo(sender, info);
        }
    }

    private void sendOnlineSeenInfo(CommandSource sender, Player target) {
        LocalDateTime login = this.plugin.getSQLManager().getLastLogin(target.getUniqueId());
        if (login == null) {
            CommandMessages.SEEN_ERROR.send(sender);
            return;
        }

        CommandMessages.SEEN_STATUS.send(sender,
                new Placeholder("%player%", target.getUsername()),
                new MessagePlaceholder("%state%", CommandMessages.ONLINE),
                new Placeholder("%time%", TextUtils.formatSeconds(sender, (int) login.until(LocalDateTime.now(), ChronoUnit.SECONDS))));
    }

    private void sendOfflineSeenInfo(CommandSource sender, PlayerInfo info) {
        LocalDateTime logout = this.plugin.getSQLManager().getLastLogout(info.getUniqueId());
        if (logout == null) {
            CommandMessages.GENERAL_INVALID_PLAYER.send(sender, new Placeholder("%player%", info.getName()));
            return;
        }

        CommandMessages.SEEN_STATUS.send(sender,
                new Placeholder("%player%", info.getName()),
                new MessagePlaceholder("%state%", CommandMessages.OFFLINE),
                new Placeholder("%time%", TextUtils.formatSeconds(sender, (int) logout.until(LocalDateTime.now(), ChronoUnit.SECONDS))));
    }
}
