package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import litebans.api.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.libs.player.PlayerLookupInfo;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.*;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.ComponentPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;
import nl.scoutcraft.eagle.proxy.utils.VersionParser;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static nl.scoutcraft.eagle.proxy.utils.TextUtils.formatSeconds;

public class LookupCommand implements ICommand, ICommandExecutor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EagleProxy plugin;

    public LookupCommand(EagleProxy plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandBuilder build() {
        return ICommand.builder("lookup")
                .permission(Perms.LOOKUP)
                .usage("/lookup <player>")
                .child(Args.offlinePlayer("target"), this);
    }

    @Override
    public void execute(CommandSource sender, CommandContext context) throws CommandException {
        final PlayerInfo info = context.<PlayerInfo>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", context.getRaw("target"))));
        final Optional<Player> player = CommandUtils.find(info.getName());
        final Optional<ScoutPlayer> sp = player.map(this.plugin.getPlayerManager()::getScoutPlayer);

        this.sendPlayerInfo(sender, info, this.plugin.getSQLManager().lookup(info), player, sp);
    }

/*
    LOOKUP
    Username » NAME
    Nickname » NICKNAME
    UUID » UUID -> CLICKABLE
    DiscordId » -> CLICKABLE
    Status » ONLINE/OFFLINE
    Lang » LANG
    Version » VERSION
    IP » IP
    Alts » ALTS
    Joined » JOINED DATA + TIME
    Last Login » LAST DATA + TIME
    Last Logout » LAST DATA + TIME
    Playtime » PLAYTIME
    Rank » RANK
    Banned » BANNED
    Muted » MUTED
*/

    private void sendPlayerInfo(CommandSource sender, PlayerInfo info, PlayerLookupInfo lookup, Optional<Player> found, Optional<ScoutPlayer> sp) throws CommandException {
        if (lookup == null)
            throw new MessageException(CommandMessages.GENERAL_INVALID_PLAYER, new Placeholder("%player%", info.getName()));

        String username = lookup.getName();
        String displayName = sp.flatMap(ScoutPlayer::getDisplayName).orElse(lookup.getDisplayName() != null ? lookup.getDisplayName() : "Unknown");
        UUID uuid = lookup.getId();
        String discordId = sp.flatMap(ScoutPlayer::getDiscordId).orElse(lookup.getDiscordId() != null ? lookup.getDiscordId() : "Unknown");
        IMessage status = (lookup.isOnline() ? CommandMessages.ONLINE : CommandMessages.OFFLINE);
        String language = lookup.getLanguage() != null ? Locale.forLanguageTag(lookup.getLanguage()).getDisplayName() : "Unknown";
        String version = found.map(p -> VersionParser.parse(p.getProtocolVersion().getProtocol())).orElse(lookup.getVersion() != 0 ? VersionParser.parse(lookup.getVersion()): "Unknown");
        String ip = found.map(p -> p.getRemoteAddress().getAddress().toString().replaceFirst("/", "")).orElse(lookup.getIp() != null ? lookup.getIp() : "Unknown");
        String alts = lookup.getAlts() != null ? lookup.getAlts() : "Unknown";
        String joined = lookup.getFirstLogin() != null ? lookup.getFirstLogin().format(FORMATTER) : "Unknown";
        String lastLogin = lookup.getLastLogin() != null ? lookup.getLastLogin().format(FORMATTER) : "Unknown";
        String lastLogout = lookup.getLastLogout() != null ? lookup.getLastLogout().format(FORMATTER) : "Unknown";
        String playtime = formatSeconds(sender, lookup.getPlaytime());
        String rank = this.getRank(uuid, "Unknown");
        boolean banned = Database.get().isPlayerBanned(uuid, ip);
        boolean muted = Database.get().isPlayerMuted(uuid, ip);

        sender.sendMessage(Component.text(' '));
        sender.sendMessage(TextUtils.line(79));
        CommandMessages.LOOKUP_HEADER.send(sender, new Placeholder("%player%", username));
        sender.sendMessage(TextUtils.line(79));
        CommandMessages.LOOKUP_USERNAME.send(sender, new Placeholder("%username%", username));
        CommandMessages.LOOKUP_DISPLAYNAME.send(sender, new Placeholder("%nickname%", displayName));
        CommandMessages.LOOKUP_UUID.send(sender, new ComponentPlaceholder("%uuid%", this.getUuidComponent(sender, uuid)));
        CommandMessages.LOOKUP_DISCORD.send(sender, new ComponentPlaceholder("%discord%", this.getDiscordComponent(sender, discordId)));
        CommandMessages.LOOKUP_STATUS.send(sender, new MessagePlaceholder("%status%", status));
        CommandMessages.LOOKUP_LANG.send(sender, new Placeholder("%language%", language));
        CommandMessages.LOOKUP_VERSION.send(sender, new Placeholder("%version%", version));
        CommandMessages.LOOKUP_IP.send(sender, new Placeholder("%ip%", ip));
        CommandMessages.LOOKUP_ALTS.send(sender, new ComponentPlaceholder("%alts%", this.getAltsComponent(sender, username, alts)));
        CommandMessages.LOOKUP_JOINED.send(sender, new Placeholder("%joined%", joined));
        CommandMessages.LOOKUP_LAST_LOGIN.send(sender, new Placeholder("%lastLogin%", lastLogin));
        CommandMessages.LOOKUP_LAST_LOGOUT.send(sender, new Placeholder("%lastLogout%", lastLogout));
        CommandMessages.LOOKUP_PLAYTIME.send(sender, new Placeholder("%playtime%", playtime));
        CommandMessages.LOOKUP_RANK.send(sender, new Placeholder("%rank%", TextUtils.capitalize(rank)));
        CommandMessages.LOOKUP_BANNED.send(sender, new Placeholder("%banned%", TextUtils.capitalize(String.valueOf(banned))));
        CommandMessages.LOOKUP_MUTED.send(sender, new Placeholder("%muted%", TextUtils.capitalize(String.valueOf(muted))));
        sender.sendMessage(TextUtils.line(79));
    }

    private Component getAltsComponent(CommandSource sender, String username, String altsString) {
        String[] alts = altsString.split(", ");

        if (alts.length == 0)
            return Component.text("Unknown");

        Component comp = this.getAltText(sender, username, alts[0]);
        for (int i = 1; i < alts.length; i++)
            comp = comp.append(Component.text(", ")).append(this.getAltText(sender, username, alts[i]));

        return comp;
    }

    private Component getAltText(CommandSource sender, String username, String alt) {
        Component base = Component.text(alt);
        if (alt.equalsIgnoreCase(username) || alt.equalsIgnoreCase("unknown"))
            return base;

        return base
                .hoverEvent(HoverEvent.showText(CommandMessages.LOOKUP_ALTS_HOVER.get(sender, new Placeholder("%player%", alt))))
                .clickEvent(ClickEvent.runCommand("/lookup " + alt));
    }

    private Component getUuidComponent(CommandSource sender, UUID uuid) {
        return Component.text(uuid.toString())
                .hoverEvent(HoverEvent.showText(CommandMessages.LOOKUP_UUID_HOVER.get(sender)))
                .clickEvent(ClickEvent.copyToClipboard(uuid.toString()));
    }

    private Component getDiscordComponent(CommandSource sender, String discordId) {
        return Component.text(discordId)
                .hoverEvent(HoverEvent.showText(CommandMessages.LOOKUP_DISCORD_HOVER.get(sender)))
                .clickEvent(ClickEvent.copyToClipboard(discordId));
    }

    private String getRank(UUID uuid, String defaultValue) {
        UserManager manager = LuckPermsProvider.get().getUserManager();
        User user = manager.getUser(uuid);
        if (user == null) {
            try {
                user = manager.loadUser(uuid).get();
            } catch (InterruptedException | ExecutionException exc) {
                this.plugin.getLogger().error("Failed to load LuckPerms user!", exc);
            }
        }

        return user != null ? user.getPrimaryGroup() : defaultValue;
    }
}
