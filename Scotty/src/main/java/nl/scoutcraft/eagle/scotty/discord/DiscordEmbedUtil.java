package nl.scoutcraft.eagle.scotty.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.LocalDateTime;

public class DiscordEmbedUtil {

    private static final Color DARK_RED = new Color(252, 3, 3);
    private static final Color RED = new Color(210, 50, 25);
    private static final Color YELLOW = new Color(255, 170, 0);
    private static final Color LIGHT_GREEN = new Color(3, 252, 94);
    private static final Color GREEN = new Color(0, 165, 81);

    public static MessageEmbed serverState(String serverName, String serverIp, int port, boolean online) {
        return new EmbedBuilder()
                .setColor(online ? LIGHT_GREEN : DARK_RED)
                .setAuthor("Eagle", null, "https://i.ibb.co/XCybG1R/LSBG-LOGO.png")
                .setTitle("**Server Administratie**")
                .addField("Naam", serverName, true)
                .addField("IP-Adres", serverIp, true)
                .addField("Poort", Integer.toString(port), true)
                .setFooter("Status: " + (online ? "Online" : "Offline"))
                .setTimestamp(LocalDateTime.now())
                .build();
    }

    public static MessageEmbed expired() {
        return new EmbedBuilder()
                .setTitle("JOUW TOKEN IS VERLOPEN!", null)
                .setColor(RED)
                .setDescription("Je kunt een nieuwe aanvragen met het commando **!register**")
                .build();
    }

    public static MessageEmbed register(String key) {
        return new EmbedBuilder()
                .setTitle("JOUW UNIEKE TOKEN.", null)
                .setColor(GREEN)
                .setDescription("Join de ScoutCraft Minecraft server en typ **/register " + key + " ** in!")
                .build();
    }

    public static MessageEmbed success(String succesTitle, String succesDesc) {
        return new EmbedBuilder()
                .setTitle(succesTitle) // Messages.REGISTER_DISCORD_SUCCESS_TITLE.getString(player), null)
                .setColor(GREEN)
                .setDescription(succesDesc) // Messages.REGISTER_DISCORD_SUCCESS_DESC.getString(player, new Placeholder("%player%", player.getUsername())))
                .build();
    }

    public static MessageEmbed helpop(String requester, @Nullable String handler, String server, String world, Integer x, Integer y, Integer z, String message, LocalDateTime timestamp, boolean closed) {
        Color color = GREEN;
        String state = "Gesloten";

        if (!closed) {
            if (handler != null) {
                color = YELLOW;
                state = "In Behandeling";
            } else {
                color = RED;
                state = "Open";
            }
        }

        return new EmbedBuilder()
                .setColor(color)
                .setAuthor("Eagle", null, "https://i.ibb.co/XCybG1R/LSBG-LOGO.png")
                .setTitle("**Server Moderatie**")
                .setDescription("**HELPOP DETAILS**")
                .addField("Speler", requester, true)
                .addField("Leiding", (handler == null ? "Geen" : handler), true)
                .addBlankField(true)
                .addField("Server", server, true)
                .addField("Wereld", (world == null ? "Onbekend" : world), true)
                .addField("Coördinaten", (x == null || y == null || z == null ? "Onbekend" : x + " " + y + " " + z), true)
                .addField("Bericht", message, false)
                .setFooter("Status: " + state)
                .setTimestamp(timestamp)
                .build();
    }

    public static MessageEmbed report(String reporter, String reportee, @Nullable String handler, String server, String message, LocalDateTime timestamp, boolean closed) {
        Color color = GREEN;
        String state = "Gesloten";

        if (!closed) {
            if (handler != null) {
                color = YELLOW;
                state = "In Behandeling";
            } else {
                color = RED;
                state = "Open";
            }
        }

        return new EmbedBuilder()
                .setColor(color)
                .setAuthor("Eagle", null, "https://i.ibb.co/XCybG1R/LSBG-LOGO.png")
                .setTitle("**Server Moderatie**")
                .setDescription("**REPORT DETAILS**")
                .addField("Reporter", reporter, true)
                .addField("Reportee", reportee, true)
                .addField("Leiding", handler == null ? "Geen" : handler, true)
                .addField("Server", server, true)
                .addField("Bericht", message, false)
                .setFooter("Status: " + state)
                .setTimestamp(timestamp)
                .build();
    }

    public static MessageEmbed bug(String reporter, @Nullable String handler, String server, String message, @Nullable String resolvedMessage, LocalDateTime timestamp, boolean closed) {
        Color color = GREEN;
        String state = "Gesloten";

        if (!closed) {
            if (handler != null) {
                color = YELLOW;
                state = "In Behandeling";
            } else {
                color = RED;
                state = "Open";
            }
        }

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(color)
                .setAuthor("Eagle", null, "https://i.ibb.co/XCybG1R/LSBG-LOGO.png")
                .setTitle("**Server Moderatie**")
                .setDescription("**BUG DETAILS**")
                .addField("Reporter", reporter, true)
                .addField("Leiding", handler == null ? "Geen" : handler, true)
                .addField("Server", server, true)
                .addField("Bericht", message, false);

        if (resolvedMessage != null)
            builder.addField("Leiding Bericht", resolvedMessage, false);

        return builder.setFooter("Status: " + state).setTimestamp(timestamp).build();
    }
}
