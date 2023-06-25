package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;

import java.util.Optional;

public class UnregisterCommand implements ICommand, IPlayerCommandExecutor {

    private final EagleProxy plugin;

    public UnregisterCommand(EagleProxy plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandBuilder build() {
        return ICommand.builder("unregister")
                .permission(Perms.DISCORD_UNREGISTER)
                .executor((s, c) -> CommandMessages.UNREGISTER_CONFIRM.send(s))
                .child(this, "confirm")
                .child(ICommand.node(Args.offlinePlayer("target"))
                        .permission(Perms.DISCORD_UNREGISTER_OTHER)
                        .executor(this::sendConfirmOther)
                        .child(this::unregisterOther, "confirm"));
    }

    @Override
    public void execute(Player player, CommandContext context) throws CommandException {
        ScoutPlayer sp = this.plugin.getPlayerManager().getScoutPlayer(player);
        String discordId = sp.getDiscordId().orElseThrow(() -> new MessageException(CommandMessages.UNREGISTER_NOT_REGISTERED));

        this.plugin.getSQLManager().unregisterDiscord(player.getUniqueId());
        sp.setDiscordId(null);
        CommandMessages.UNREGISTER_SUCCESS.send(player);

        this.plugin.getDiscordManager().sendMessage(discordId, CommandMessages.UNREGISTER_DISCORD_SUCCESS.getString(player, new Placeholder("%player%", player.getUsername())));
    }

    private void sendConfirmOther(CommandSource sender, CommandContext context) {
        CommandMessages.UNREGISTER_CONFIRM_OTHER.send(sender, new Placeholder("%player%", context.getRaw("target")));
    }

    private void unregisterOther(CommandSource sender, CommandContext context) throws MessageException {
        PlayerInfo target = context.<PlayerInfo>get("target").get();

        Optional<Player> found = CommandUtils.find(target.getName());
        if (found.isPresent()) {
            this.unregisterOther(found.get());
            CommandMessages.UNREGISTER_SUCCESS_OTHER.send(sender, new Placeholder("%player%", found.get().getUsername()));
            return;
        }

        String discordId = this.plugin.getSQLManager().getDiscordId(target.getName());
        if (discordId != null) {
            this.unregister(discordId);
            CommandMessages.UNREGISTER_SUCCESS_OTHER.send(sender, new Placeholder("%player%", target.getName()));
            return;
        }

        PlayerInfo info = this.plugin.getSQLManager().getDiscordPlayerInfo(target.getName()).orElse(null);
        if (info != null) {
            this.unregister(target.getName());
            CommandMessages.UNREGISTER_SUCCESS_OTHER.send(sender, new Placeholder("%player%", info.getName()));
            return;
        }

        CommandMessages.GENERAL_INVALID_PLAYER.send(sender, new Placeholder("%player%", target.getName()));
    }

    private void unregisterOther(Player found) throws MessageException {
        ScoutPlayer sp = this.plugin.getPlayerManager().getScoutPlayer(found);
        this.unregister(sp.getDiscordId().orElseThrow(() -> new MessageException(CommandMessages.UNREGISTER_NOT_REGISTERED_OTHER, new Placeholder("%player%", found.getUsername()))));
        CommandMessages.UNREGISTER_SUCCESS_BY_ADMIN.send(found);
    }

    private void unregister(String discordId) {
        this.plugin.getSQLManager().unregisterDiscord(discordId);
    }
}
