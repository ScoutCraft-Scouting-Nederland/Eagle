package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
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
import nl.scoutcraft.eagle.proxy.io.DatabaseManager;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.PlayerManager;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;

public class NickCommand implements ICommand, IPlayerCommandExecutor {

    private final PlayerManager playerManager = EagleProxy.getInstance().getPlayerManager();
    private final DatabaseManager sqlManager = EagleProxy.getInstance().getSQLManager();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("nickname", "nick")
                .permission(Perms.NICKNAME)
                .usage("/nickname [player] {<name>|off}")
                .child(ICommand.node("off").executor((IPlayerCommandExecutor) this::executeOff))   // .maxArgs(1)
                .child(ICommand.node(Args.offlinePlayer("target"))
                        .permission(Perms.NICKNAME_OTHER)
                        .usage("/nickname [player] {<name>|off}")
                        .child(ICommand.node("off").executor(this::executeOtherOff))
                        .child(ICommand.node(Args.string("name")).executor(this::executeOther))
                )
                .child(ICommand.node(Args.string("name")).executor(this));  // .maxArgs(1)
    }

    @Override
    public void execute(Player player, CommandContext context) throws MessageException {
        String name = PlainTextComponentSerializer.plainText().serialize(TextUtils.colorize(context.<String>get("name").get()));
        ScoutPlayer sp = this.playerManager.getScoutPlayer(player);

        if (name.length() < 3)
            throw new MessageException(CommandMessages.NICK_TOO_SHORT, new Placeholder("%nickname%", name));

        if (name.equals(player.getUsername()))
            throw new MessageException(CommandMessages.NICK_SAME_AS_NAME);

        if (name.equalsIgnoreCase(sp.getDisplayName().orElse(null)))
            throw new MessageException(CommandMessages.NICK_ALREADY_SET, new Placeholder("%nickname%", name));

        PlayerInfo info = this.sqlManager.getPlayerInfo(name);
        if (info != null && !info.getUniqueId().equals(player.getUniqueId()))
            throw new MessageException(CommandMessages.NICK_NOT_ALLOWED, new Placeholder("%nickname%", name));

        this.playerManager.setDisplayName(player, name);
        CommandMessages.NICK_SET_TO.send(player, new Placeholder("%nickname%", name));
    }

    private void executeOff(Player player, CommandContext context) throws CommandException {
        ScoutPlayer sp = this.playerManager.getScoutPlayer(player);

        if (sp.getDisplayName().isEmpty() || player.getUsername().equals(sp.getDisplayName().get()))
            throw new MessageException(CommandMessages.NICK_NOT_CHANGED);

        this.playerManager.setDisplayName(player, null);
        CommandMessages.NICK_DISABLED.send(player);
    }

    private void executeOther(CommandSource sender, CommandContext context) throws MessageException {
        PlayerInfo target = context.<PlayerInfo>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        String name = PlainTextComponentSerializer.plainText().serialize(TextUtils.colorize(context.<String>get("name").get()));

        PlayerInfo info = this.sqlManager.getPlayerInfo(name);
        if (info != null && !info.getUniqueId().equals(target.getUniqueId()))
            throw new MessageException(CommandMessages.NICK_NOT_ALLOWED, new Placeholder("%nickname%", name));

        Player targetPlayer = EagleProxy.getProxy().getPlayer(target.getUniqueId()).orElse(null);
        if (targetPlayer != null) {
            this.playerManager.setDisplayName(targetPlayer, name);
            CommandMessages.NICK_SET_TO.send(targetPlayer, new Placeholder("%nickname%", name));
        } else {
            this.sqlManager.setDisplayName(target.getUniqueId(), name); //
        }

        CommandMessages.NICK_SET_TO_OTHER.send(sender, new Placeholder("%player%", target.getName()), new Placeholder("%nickname%", name));
    }

    private void executeOtherOff(CommandSource sender, CommandContext context) throws MessageException {
        PlayerInfo target = context.<PlayerInfo>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));

        Player targetPlayer = EagleProxy.getProxy().getPlayer(target.getUniqueId()).orElse(null);
        if (targetPlayer != null) {
            this.playerManager.setDisplayName(targetPlayer, null);
            CommandMessages.NICK_DISABLED.send(targetPlayer);
        } else {
            this.sqlManager.setDisplayName(target.getUniqueId(), null);
        }

        CommandMessages.NICK_DISABLED_OTHER.send(sender, new Placeholder("%player%", target.getName()));
    }
}
