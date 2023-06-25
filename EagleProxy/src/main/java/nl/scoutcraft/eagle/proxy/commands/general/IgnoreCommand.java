package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.IgnoreList;

import java.time.LocalDateTime;

public class IgnoreCommand implements ICommand, IPlayerCommandExecutor {

    private static final EagleProxy PLUGIN = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("ignore", "silence")
                .permission(Perms.IGNORE)
                .usage("/ignore <player>")
                .child(Args.player("target"), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws MessageException {
        LocalDateTime timestamp = LocalDateTime.now();
        Player ignoree = context.<Player>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));

        if (ignoree.hasPermission(Perms.IGNORE_EXEMPT))
            throw new MessageException(CommandMessages.IGNORE_EXEMPT, new Placeholder("%ignoree%", ignoree.getUsername()));

        IgnoreList ignores = PLUGIN.getChatManager().getIgnores();
        if (ignores.isIgnoring(player.getUniqueId(), ignoree.getUniqueId()))
            throw new MessageException(CommandMessages.IGNORE_ALREADY_IGNORED, new Placeholder("%ignoree%", ignoree.getUsername()));

        ignores.addIgnore(player.getUniqueId(), new PlayerInfo(ignoree.getUniqueId(), ignoree.getUsername(), null), timestamp);
        CommandMessages.IGNORE_IGNORED.send(player, new Placeholder("%ignoree%", ignoree.getUsername()));
    }
}
