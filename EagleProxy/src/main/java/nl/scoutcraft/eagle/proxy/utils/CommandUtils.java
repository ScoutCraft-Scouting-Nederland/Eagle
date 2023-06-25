package nl.scoutcraft.eagle.proxy.utils;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.CommandException;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.player.obj.PlayerManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandUtils {

    @NotNull
    public static <T extends Collection<? super String>> T copyPartialMatches(@NotNull final String token, @NotNull final Iterable<String> originals, @NotNull final T collection) throws UnsupportedOperationException, IllegalArgumentException {
        Validate.notNull(token, "Search token cannot be null");
        Validate.notNull(collection, "Collection cannot be null");
        Validate.notNull(originals, "Originals cannot be null");

        for (String string : originals)
            if (startsWithIgnoreCase(string, token))
                collection.add(string);

        return collection;
    }

    public static boolean startsWithIgnoreCase(@NotNull final String string, @NotNull final String prefix) throws IllegalArgumentException, NullPointerException {
        Validate.notNull(string, "Cannot check a null string for a match");
        if (string.length() < prefix.length())
            return false;

        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static Component buildMessage(String[] args, int startIndex, boolean colorize) {
        String builder = IntStream.range(startIndex + 1, args.length).mapToObj(i -> ' ' + args[i]).collect(Collectors.joining("", args[startIndex], ""));

        String str = StringUtils.trim(builder);
        return colorize ? TextUtils.colorize(str) : Component.text(str);
    }

    public static Optional<Player> find(String name) {
        PlayerManager manager = EagleProxy.getInstance().getPlayerManager();
        Collection<Player> players = EagleProxy.getProxy().getAllPlayers();

        return players.stream().filter(p -> p.getUsername().equalsIgnoreCase(name) || name.equalsIgnoreCase(manager.getScoutPlayer(p).getDisplayName().orElse(null))).findFirst();
    }

    public static void globalTeleport(@NotNull Player a, @NotNull Player b) throws CommandException {
        if (a.getUniqueId().equals(b.getUniqueId()))
            throw new MessageException(CommandMessages.GTP_TP_TO_SELF);

        b.getCurrentServer().ifPresent(s -> EagleProxy.getInstance().getPlayerManager().getPlayerChannel().request().setTarget(s.getServer()).teleport(a.getUniqueId(), b.getUniqueId()));

        if (!a.getCurrentServer().map(ServerConnection::getServer).equals(b.getCurrentServer().map(ServerConnection::getServer)))
            b.getCurrentServer().ifPresent(s -> a.createConnectionRequest(s.getServer()).fireAndForget());
    }

    public static List<String> getIgnorees(UUID uuid) {
        return EagleProxy.getInstance().getChatManager().getIgnores().getIgnores(uuid).stream().map(PlayerInfo::getName).collect(Collectors.toList());
    }
}
