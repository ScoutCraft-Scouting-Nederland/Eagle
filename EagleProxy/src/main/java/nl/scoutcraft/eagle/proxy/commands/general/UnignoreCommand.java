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

public class UnignoreCommand implements ICommand, IPlayerCommandExecutor {

    private final EagleProxy plugin;

    public UnignoreCommand(EagleProxy plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandBuilder build() {
        return ICommand.builder("unignore", "unsilence")
                .permission(Perms.IGNORE)
                .usage("/unignore <player>")
                .child(Args.ignoree("ignoree"), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws MessageException {
        PlayerInfo ignoree = context.<PlayerInfo>get("ignoree").orElseThrow(() -> new MessageException(CommandMessages.UNIGNORE_NOT_IGNORED));
        if (!this.plugin.getChatManager().getIgnores().isIgnoring(player.getUniqueId(), ignoree.getUniqueId()))
            throw new MessageException(CommandMessages.UNIGNORE_NOT_IGNORED, new Placeholder("%ignoree%", context.getRaw("ignoree")));

        this.plugin.getChatManager().getIgnores().removeIgnore(player.getUniqueId(), ignoree.getUniqueId());
        CommandMessages.UNIGNORE_UNIGNORED.send(player, new Placeholder("%ignoree%", ignoree.getName()));
    }
}
