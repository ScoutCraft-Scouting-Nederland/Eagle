package nl.scoutcraft.eagle.proxy.discord;

import java.time.LocalDateTime;

public enum DiscordEmbedType {

    EXPIRED(0),
    SERVER_STATE(1, String.class, String.class, int.class, boolean.class),
    REGISTER(2, String.class),
    SUCCESS(3, String.class, String.class),
    HELPOP(4, String.class, String.class, String.class, String.class, Integer.class, Integer.class, Integer.class, String.class, LocalDateTime.class, boolean.class),
    REPORT(5, String.class, String.class, String.class, String.class, String.class, LocalDateTime.class, Boolean.class),
    BUG(6, String.class, String.class, String.class, String.class, String.class, LocalDateTime.class, Boolean.class);

    private final int id;
    private final Class<?>[] params;

    DiscordEmbedType(int id, Class<?>... params) {
        this.id = id;
        this.params = params;
    }

    public int getId() {
        return this.id;
    }

    public Class<?>[] getParams() {
        return this.params;
    }
}
