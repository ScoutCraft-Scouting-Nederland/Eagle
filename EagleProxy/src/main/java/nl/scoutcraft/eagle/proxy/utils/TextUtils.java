package nl.scoutcraft.eagle.proxy.utils;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TextUtils {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter HUMAN_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static String formatSeconds(CommandSource target, int seconds) {
        int years = seconds / 31_536_000;
        seconds %= 31_536_000;

        int months = seconds / 2_628_000;
        seconds %= 2_628_000;

        int weeks = seconds / 604_800;
        seconds %= 604_800;

        int days = seconds / 86_400;
        seconds %= 86_400;

        int hours = seconds / 3_600;
        seconds %= 3_600;

        int minutes = seconds / 60;
        seconds %= 60;

        StringBuilder output = new StringBuilder();
        if (years != 0)
            output.append(years).append(" ").append((years > 1 ? CommandMessages.YEARS : CommandMessages.YEAR).getString(target)).append(" ");
        if (months != 0)
            output.append(months).append(" ").append((months > 1 ? CommandMessages.MONTHS : CommandMessages.MONTH).getString(target)).append(" ");
        if (weeks != 0)
            output.append(weeks).append(" ").append((weeks > 1 ? CommandMessages.WEEKS : CommandMessages.WEEK).getString(target)).append(" ");
        if (days != 0)
            output.append(days).append(" ").append((days > 1 ? CommandMessages.DAYS : CommandMessages.DAY).getString(target)).append(" ");
        if (hours != 0)
            output.append(hours).append(" ").append((hours > 1 ? CommandMessages.HOURS : CommandMessages.HOUR).getString(target)).append(" ");
        if (minutes != 0)
            output.append(minutes).append(" ").append((minutes > 1 ? CommandMessages.MINUTES : CommandMessages.MINUTE).getString(target)).append(" ");
        output.append(seconds).append(" ").append((seconds != 1 ? CommandMessages.SECONDS : CommandMessages.SECOND).getString(target));
        return output.toString();
    }

    public static String formatTimeAgo(Player player, LocalDateTime timestamp) {
        long seconds = timestamp.until(LocalDateTime.now(), ChronoUnit.SECONDS);

        if (seconds > 31_536_000)
            return formatTimeAgo(player, CommandMessages.YEARS, CommandMessages.YEAR, seconds / 31_536_000);
        else if (seconds > 2_628_000)
            return formatTimeAgo(player, CommandMessages.MONTHS, CommandMessages.MONTH, seconds / 2_628_000);
        else if (seconds > 604_800)
            return formatTimeAgo(player, CommandMessages.WEEKS, CommandMessages.WEEK, seconds / 604_800);
        else if (seconds > 86_400)
            return formatTimeAgo(player, CommandMessages.DAYS, CommandMessages.DAY, seconds / 86_400);
        else if (seconds > 3_600)
            return formatTimeAgo(player, CommandMessages.HOURS, CommandMessages.HOUR, seconds / 3_600);
        else if (seconds > 60)
            return formatTimeAgo(player, CommandMessages.MINUTES, CommandMessages.MINUTE, seconds / 60);
        else return formatTimeAgo(player, CommandMessages.SECONDS, CommandMessages.SECOND, seconds);
    }

    private static String formatTimeAgo(Player player, IMessage multi, IMessage single, long value) {
        return value + " " + (value != 1 ? multi.getString(player) : single.getString(player));
    }
}
