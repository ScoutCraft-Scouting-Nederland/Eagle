package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static nl.scoutcraft.eagle.proxy.utils.TextUtils.formatSeconds;

public class PlaytimeCommand implements ICommand, ICommandExecutor {

    private static final long UPDATE_CACHE_MILLIS = 60_000;

    private final EagleProxy plugin;

    private Map<String, Integer> topCache;
    private long topLastUpdate;

    public PlaytimeCommand(EagleProxy plugin) {
        this.plugin = plugin;
        this.topCache = Collections.emptyMap();
        this.topLastUpdate = 0L;
    }

    @Override
    public CommandBuilder build() {
        return ICommand.builder("playtime")
                .permission(Perms.PLAYTIME)
                .executor(this)
                .child(ICommand.node("top").permission(Perms.PLAYTIME_TOP).executor((s, c) -> this.sendTop(s)))
                .child(ICommand.node(Args.offlinePlayer("target")).permission(Perms.PLAYTIME_OTHER).executor(this::executeOther));
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws MessageException {
        if (!(sender instanceof Player)) {
            CommandMessages.PLAYTIME_NO_CONSOLE.send(sender);
            return;
        }

        CommandMessages.PLAYTIME_SELF.send(sender, new Placeholder("%time%", formatSeconds(sender, this.plugin.getPlayerManager().getScoutPlayer((Player) sender).getPlaytime())));
    }

    private void executeOther(CommandSource sender, CommandContext context) throws MessageException {
        PlayerInfo target = context.<PlayerInfo>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        Optional<Player> found = EagleProxy.getProxy().getPlayer(target.getUniqueId());

        if (found.isPresent()) {
            CommandMessages.PLAYTIME_OTHER.send(sender, new Placeholder("%player%", target.getName()), new Placeholder("%time%", formatSeconds(sender, this.plugin.getPlayerManager().getScoutPlayer(found.get()).getPlaytime())));
            return;
        }

        int playtime = this.plugin.getSQLManager().getPlaytime(target.getUniqueId()).orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", target.getName())));
        CommandMessages.PLAYTIME_OTHER.send(sender, new Placeholder("%player%", target.getName()), new Placeholder("%time%", formatSeconds(sender, playtime)));
    }

    private void sendTop(CommandSource sender) {
        this.updateCache();

        sender.sendMessage(Component.text(' '));
        TextUtils.text(TextUtils.line(30)).append(Component.text(" Playtime Top ", Colors.GOLD, TextDecoration.BOLD)).append(TextUtils.line(30));

        int place = 1;
        for (Map.Entry<String, Integer> entry : this.topCache.entrySet())
            CommandMessages.PLAYTIME_TOP.send(sender, new Placeholder("%number%", String.valueOf(place++)), new Placeholder("%player%", entry.getKey()), new Placeholder("%time%", formatSeconds(sender, entry.getValue())));

        sender.sendMessage(TextUtils.line(79));
    }

    private void updateCache() {
        long now = System.currentTimeMillis();

        if (this.topLastUpdate + UPDATE_CACHE_MILLIS < now) {
            this.topCache = this.plugin.getSQLManager().getPlaytimeTop();
            this.topLastUpdate = now;
        }
    }
}
