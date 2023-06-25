package nl.scoutcraft.eagle.proxy.discord;

public enum DiscordChannel {

    CHATLOG(1),
    HELPOPS(2),
    REPORTS(3),
    BUGS(4),
    MONITORING(5);

    private final int id;

    DiscordChannel(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
