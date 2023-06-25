package nl.scoutcraft.eagle.scotty.discord;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.LocalDateTime;

public final class EmbedTypes {

    public static final EmbedType EXPIRED = new EmbedType(0, args -> DiscordEmbedUtil.expired());
    public static final EmbedType SERVER_STATE = new EmbedType(1, args -> DiscordEmbedUtil.serverState((String) args[0], (String) args[1], (Integer) args[2], (Boolean) args[3]), String.class, String.class, int.class, boolean.class);
    public static final EmbedType REGISTER = new EmbedType(2, args -> DiscordEmbedUtil.register((String) args[0]), String.class);
    public static final EmbedType SUCCESS = new EmbedType(3, args -> DiscordEmbedUtil.success((String) args[0], (String) args[1]), String.class, String.class);
    public static final EmbedType HELPOP = new EmbedType(4, args -> DiscordEmbedUtil.helpop((String) args[0], (String) args[1], (String) args[2], (String) args[3], (Integer) args[4], (Integer) args[5], (Integer) args[6], (String) args[7], (LocalDateTime) args[8], (Boolean) args[9]), String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class, String.class, LocalDateTime.class, boolean.class);
    public static final EmbedType REPORT = new EmbedType(5, args -> DiscordEmbedUtil.report((String) args[0], (String) args[1], (String) args[2], (String) args[3], (String) args[4], (LocalDateTime) args[5], (Boolean) args[6]), String.class, String.class, String.class, String.class, String.class, LocalDateTime.class, Color.class, String.class, Boolean.class);
    public static final EmbedType BUG = new EmbedType(6, args -> DiscordEmbedUtil.bug((String) args[0], (String) args[1], (String) args[2], (String) args[3], (String) args[4], (LocalDateTime) args[5], (Boolean) args[6]), String.class, String.class, String.class, String.class, String.class, LocalDateTime.class, Color.class, String.class, Boolean.class);

    private static final EmbedType[] VALUES = { EXPIRED, SERVER_STATE, REGISTER, SUCCESS, HELPOP, REPORT, BUG };

    @Nullable
    public static EmbedType of(int id) {
        for (EmbedType type : VALUES)
            if (type.getId() == id)
                return type;

        return null;
    }

    private EmbedTypes() {}
}
