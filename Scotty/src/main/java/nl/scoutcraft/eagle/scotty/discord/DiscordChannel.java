package nl.scoutcraft.eagle.scotty.discord;

import nl.scoutcraft.eagle.scotty.Scotty;
import nl.scoutcraft.eagle.scotty.config.ConfigAdaptor;
import org.jetbrains.annotations.Nullable;

public enum DiscordChannel {

    CHATLOG(1, "channels", "chatlog"),
    HELPOPS(2, "channels", "helpops"),
    REPORTS(3, "channels", "reports"),
    BUGS(4, "channels", "bugs"),
    MONITORING(5, "channels", "monitoring");

    private final int id;
    private final Object[] configPath;
    @Nullable private String channelId;

    DiscordChannel(int id, Object... configPath) {
        this.id = id;
        this.configPath = configPath;
    }

    public String getChannelId() {
        if (this.channelId == null)
            this.load(Scotty.getInstance().getConfig());
        return this.channelId;
    }

    public void load(ConfigAdaptor config) {
        this.channelId = config.getNode(this.configPath).getString("");
    }

    public static DiscordChannel of(int id) {
        for (DiscordChannel channel : values())
            if (channel.id == id)
                return channel;

        return null;
    }
}
