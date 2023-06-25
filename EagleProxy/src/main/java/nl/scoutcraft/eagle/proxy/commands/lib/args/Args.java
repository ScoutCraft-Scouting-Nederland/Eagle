package nl.scoutcraft.eagle.proxy.commands.lib.args;

import com.google.common.collect.Lists;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;
import nl.scoutcraft.eagle.proxy.server.ScoutServer;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class Args {

    public static Argument<String> path(String name, String... aliases) {
        return path(name, false, aliases);
    }

    public static Argument<String> path(String name, boolean silent, String... aliases) {
        return new PathArgument(name, silent, aliases);
    }

    public static Argument<String> string(String name, String... defaults) {
        return new StringArgument(name, Lists.newArrayList(defaults));
    }

    public static Argument<Integer> integer(String name) {
        return new IntegerArgument(name);
    }

    public static Argument<Double> number(String name) {
        return new NumberArgument(name);
    }

    public static Argument<Integer> port(String name) {
        return new PortArgument(name);
    }

    public static Argument<Player> player(String name) {
        return new PlayerArgument(name);
    }

    public static Argument<ScoutPlayer> scoutPlayer(String name) {
        return new ScoutPlayerArgument(name);
    }

    public static Argument<Player> nickname(String name) {
        return new NicknameArgument(name);
    }

    public static Argument<PlayerInfo> offlinePlayer(String name) {
        return new OfflinePlayerArgument(name);
    }

    public static Argument<PlayerInfo> ignoree(String name) {
        return new IgnoreeArgument(name);
    }

    public static Argument<RegisteredServer> server(String name) {
        return new ServerArgument(name);
    }

    public static Argument<ScoutServer> scoutServer(String name) {
        return new ScoutServerArgument(name);
    }

    public static Argument<String> serverGroup(String name) {
        return new ServerGroupArgument(name);
    }

    public static Argument<UUID> uuid(String name) {
        return new UUIDArgument(name);
    }

    public static Argument<Locale> locale(String name) {
        return new LocaleArgument(name);
    }

    public static <T> ChoiceArgument<T> choices(String name, Map<String, T> choices) {
        return new ChoiceArgument<>(name, choices);
    }

    private Args(){}
}
