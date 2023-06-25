package nl.scoutcraft.eagle.proxy.commands.message;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.UsageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;

import java.util.HashMap;
import java.util.Map;

public class SocialSpyCommand implements ICommand, IPlayerCommandExecutor {

    private static final String USAGE = "/socialspy {on|off}";

    @Override
    public CommandBuilder build() {
        Map<String, Boolean> states = new HashMap<>();
        states.put("on", true);
        states.put("off", false);

        return ICommand.builder("socialspy", "spy")
                .permission(Perms.SOCIAL_SPY)
                .usage(USAGE)
                .child(Args.choices("state", states), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        boolean value = context.<Boolean>get("state").orElseThrow(() -> new UsageException(USAGE));
        EagleProxy.getInstance().getPlayerManager().setSpying(player, value);
        CommandMessages.SOCIALSPY_SUCCESS.send(player, new MessagePlaceholder("%state%", (value ? CommandMessages.ENABLED : CommandMessages.DISABLED)));
    }
}
