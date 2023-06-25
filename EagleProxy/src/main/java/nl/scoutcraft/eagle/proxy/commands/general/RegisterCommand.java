package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.discord.DiscordEmbeds;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

import java.time.LocalDateTime;

public class RegisterCommand implements ICommand, IPlayerCommandExecutor {

    private final EagleProxy plugin;

    public RegisterCommand(EagleProxy plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandBuilder build() {
        return ICommand.builder("register")
                .permission(Perms.DISCORD_REGISTER)
                .usage("/register <token>")
                .child(Args.string("token"), this);
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        String token = context.<String>get("token").get();
        String[] values = this.plugin.getDiscordManager().removeToken(token);

        if (values == null)
            throw new MessageException(CommandMessages.GENERAL_INVALID_TOKEN, new Placeholder("%token%", token));

        String discordId = values[0];
        String embedId = values[1];
        this.plugin.getSQLManager().registerDiscord(player.getUniqueId(), discordId, LocalDateTime.now());
        CommandMessages.REGISTER_SUCCESS.send(player);

        this.plugin.getPlayerManager().getScoutPlayer(player).setDiscordId(discordId);
        this.plugin.getDiscordManager().editMessage(discordId, embedId, DiscordEmbeds.success(player));
        this.plugin.getDiscordManager().updateDiscordNameAndRoles(player);
    }
}
