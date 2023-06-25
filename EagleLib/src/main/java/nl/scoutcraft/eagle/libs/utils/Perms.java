package nl.scoutcraft.eagle.libs.utils;

public final class Perms {

    // Loadbalancer Perm
    public static final String LOADBALANCER_BYPASS = perm("loadbalancer.bypass");

    // Reload Perm
    public static final String RELOAD = perm("admin");

    // ServerManager Perms
    public static final String SERVERMANAGER = perm("servermanager");
    public static final String SERVERMANAGER_RELOAD = perm("servermanager.reload");
    public static final String SERVERMANAGER_SERVERS = perm("servermanager.servers");
    public static final String SERVERMANAGER_SERVERS_LIST = perm("servermanager.servers.list");
    public static final String SERVERMANAGER_SERVERS_EDIT = perm("servermanager.servers.edit");
    public static final String SERVERMANAGER_SERVERS_SERVER_INFO = perm("servermanager.servers.server.info");
    public static final String SERVERMANAGER_SERVERS_SERVER_EDIT = perm("servermanager.servers.server.edit");
    public static final String SERVERMANAGER_GROUPS = perm("servermanager.groups");
    public static final String SERVERMANAGER_GROUPS_LIST = perm("servermanager.groups.info");
    public static final String SERVERMANAGER_GROUPS_EDIT = perm("servermanager.groups.edit");
    public static final String SERVERMANAGER_GROUPS_GROUP_INFO = perm("servermanager.groups.group.info");
    public static final String SERVERMANAGER_GROUPS_GROUP_EDIT = perm("servermanager.groups.group.edit");

    // Maintenance Perms
    public static final String MAINTENANCE = perm("maintenance");
    public static final String MAINTENANCE_BYPASS = perm("maintenance.bypass");

    // Announce Perms
    public static final String ANNOUNCE = perm("announce");
    public static final String ANNOUNCE_CHAT = perm("announce.chat");
    public static final String ANNOUNCE_TITLE = perm("announce.title");
    public static final String ANNOUNCE_SUBTITLE = perm("announce.subtitle");
    public static final String ANNOUNCEMENTS_BYPASS = perm("announcements.bypass");

    // Chat Perms
    public static final String CHAT_LOCK = perm("chat.lock");
    public static final String CHAT_LOCK_STATUS = perm("chat.lock.status");
    public static final String CHAT_LOCK_GLOBAL = perm("chat.lock.global");
    public static final String CHAT_LOCK_SERVER = perm("chat.lock.server");
    public static final String CHAT_LOCK_BYPASS = perm("chat.lock.bypass");
    public static final String CHAT_LINK_BYPASS = perm("chat.link.bypass");
    public static final String CHAT_CAPS_BYPASS = perm("chat.caps.bypass");
    public static final String CHAT_SPAM_BYPASS = perm("chat.spam.bypass");
    public static final String CHAT_CLEAR = perm("chat.clear");
    public static final String CHAT_CLEAR_BYPASS = perm("chat.clear.bypass");
    public static final String CHAT_TEAM = perm("chat.team");
    public static final String CHAT_COLORED = perm("chat.colored");

    // Server Perms
    public static final String LOBBY = perm("lobby");
    public static final String SERVER = perm("server");
    public static final String SERVER_STATUS = perm("server.status");

    // HelpOp Perms
    public static final String HELPOP = perm("helpop");
    public static final String HELPOPS = perm("helpops");
    public static final String HELPOPS_LIST = perm("helpops.list");
    public static final String HELPOPS_ACCEPT = perm("helpops.accept");
    public static final String HELPOPS_CLOSE = perm("helpops.close");
    public static final String HELPOPS_ANNOUNCEMENT = perm("helpops.announcement");

    // Report Perms
    public static final String REPORT = perm("report");
    public static final String REPORT_EXEMPT = perm("report.exempt");
    public static final String REPORTS = perm("reports");
    public static final String REPORTS_LIST = perm("reports.list");
    public static final String REPORTS_ACCEPT = perm("reports.accept");
    public static final String REPORTS_CLOSE = perm("reports.close");
    public static final String REPORTS_ANNOUNCEMENT = perm("reports.announcement");

    // Bugs
    public static final String BUG = perm("bug");
    public static final String BUGS = perm("bugs");
    public static final String BUGS_LIST = perm("bugs.list");
    public static final String BUGS_ACCEPT = perm("bugs.accept");
    public static final String BUGS_CLOSE = perm("bugs.close");
    public static final String BUGS_ANNOUNCEMENT = perm("bugs.announcement");

    // Message Perms
    public static final String MSG = perm("msg");
    public static final String REPLY = perm("reply");
    public static final String SOCIAL_SPY = perm("socialspy");

    // Party Perms
    public static final String PARTY_CHAT = perm("party.chat");
    public static final String PARTY_CREATE = perm("party.create");
    public static final String PARTY_DISBAND = perm("party.disband");
    public static final String PARTY_HELP = perm("party.help");
    public static final String PARTY_INFO = perm("party.info");
    public static final String PARTY_INVITE = perm("party.invite");
    public static final String PARTY_JOIN = perm("party.join");
    public static final String PARTY_KICK = perm("party.kick");
    public static final String PARTY_LEAVE = perm("party.leave");
    public static final String PARTY_PRIVATE = perm("party.private");
    public static final String PARTY_PUBLIC = perm("party.public");
    public static final String PARTY_TELEPORT = perm("party.teleport");
    public static final String PARTY_TRANSFER = perm("party.transfer");
    public static final String PARTY_WARP = perm("party.warp");

    // Discord Commands
    public static final String DISCORD_REGISTER = perm("discord.register");
    public static final String DISCORD_UNREGISTER = perm("discord.unregister");
    public static final String DISCORD_UNREGISTER_OTHER = perm("discord.unregister.other");

    // Playtime perms
    public static final String PLAYTIME = perm("playtime");
    public static final String PLAYTIME_TOP = perm("playtime.top");
    public static final String PLAYTIME_OTHER = perm("playtime.other");

    // Other Commands
    public static final String FIND = perm("find");
    public static final String GLIST = perm("glist");
    public static final String LANG = perm("lang");
    public static final String LOOKUP = perm("lookup");
    public static final String NICKNAME = perm("nickname");
    public static final String NICKNAME_OTHER = perm("nickname.other");
    public static final String PING = perm("ping");
    public static final String PING_OTHER = perm("ping.other");
    public static final String REALNAME = perm("realname");
    public static final String SEEN = perm("seen");
    public static final String SEND = perm("send");
    public static final String IGNORE = perm("ignore");
    public static final String IGNORE_EXEMPT = perm("ignore.exempt");
    public static final String WHEREAMI = perm("whereami");
    public static final String GTP = perm("gtp");
    public static final String GTPHERE = perm("gtphere");

    private static String perm(String perm) {
        return "eagle." + perm;
    }

    private Perms() {}
}
