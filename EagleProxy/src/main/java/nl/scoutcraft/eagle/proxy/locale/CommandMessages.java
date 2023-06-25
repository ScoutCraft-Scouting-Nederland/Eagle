package nl.scoutcraft.eagle.proxy.locale;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.api.CompoundMessage;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.Message;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;

public final class CommandMessages {

    private static final Internationalization LANG = EagleProxy.getInstance().getLangManager().getCommandLang();

    public static final IMessage PREFIX = new Message(LANG, "prefix");
    public static final IMessage USAGE_FORMAT = new CompoundMessage(LANG, "usage_format", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NO_CONSOLE = new Message(LANG, "no_console");
    public static final IMessage NO_PERMISSION = new CompoundMessage(LANG, "no_permission", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage ONLINE = new Message(LANG, "online");
    public static final IMessage OFFLINE = new Message(LANG, "offline");
    public static final IMessage ENABLED = new Message(LANG, "enabled");
    public static final IMessage DISABLED = new Message(LANG, "disabled");

    public static final IMessage SECOND = new Message(LANG, "second");
    public static final IMessage SECONDS = new Message(LANG, "seconds");
    public static final IMessage MINUTE = new Message(LANG, "minute");
    public static final IMessage MINUTES = new Message(LANG, "minutes");
    public static final IMessage HOUR = new Message(LANG, "hour");
    public static final IMessage HOURS = new Message(LANG, "hours");
    public static final IMessage DAY = new Message(LANG, "day");
    public static final IMessage DAYS = new Message(LANG, "days");
    public static final IMessage WEEK = new Message(LANG, "week");
    public static final IMessage WEEKS = new Message(LANG, "weeks");
    public static final IMessage MONTH = new Message(LANG, "month");
    public static final IMessage MONTHS = new Message(LANG, "months");
    public static final IMessage YEAR = new Message(LANG, "year");
    public static final IMessage YEARS = new Message(LANG, "years");

    public static final IMessage GENERAL_ADD_MESSAGE = new CompoundMessage(LANG, "general.add_message", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_SERVER = new CompoundMessage(LANG, "general.invalid_server", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_PLAYER = new CompoundMessage(LANG, "general.invalid_player", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_UUID = new CompoundMessage(LANG, "general.invalid_uuid", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_GROUP = new CompoundMessage(LANG, "general.invalid_group", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_PORT = new CompoundMessage(LANG, "general.invalid_port", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_TOKEN = new CompoundMessage(LANG, "general.invalid_token", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_LANG = new CompoundMessage(LANG, "general.invalid_lang", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_HELPOP = new CompoundMessage(LANG, "general.invalid_helpop", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_REPORT = new CompoundMessage(LANG, "general.invalid_report", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GENERAL_INVALID_BUG = new CompoundMessage(LANG, "general.invalid_bug", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage EAGLE_RELOADED = new CompoundMessage(LANG, "eagle.reloaded", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage LANG_SET = new CompoundMessage(LANG, "lang.set", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage NICK_ALREADY_SET = new CompoundMessage(LANG, "nick.already_set", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_NOT_CHANGED = new CompoundMessage(LANG, "nick.not_changed", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_SAME_AS_NAME = new CompoundMessage(LANG, "nick.same_as_name", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_TOO_SHORT = new CompoundMessage(LANG, "nick.too_short", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_NOT_ALLOWED = new CompoundMessage(LANG, "nick.not_allowed", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_DISABLED = new CompoundMessage(LANG, "nick.disabled", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_SET_TO = new CompoundMessage(LANG, "nick.set_to", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_DISABLED_OTHER = new CompoundMessage(LANG, "nick.disabled.other", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NICK_SET_TO_OTHER = new CompoundMessage(LANG, "nick.set_to.other", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage WHEREAMI_MSG = new CompoundMessage(LANG, "whereami.msg", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage WHEREAMI_FAILED = new CompoundMessage(LANG, "whereami.failed", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage REALNAME_MSG = new CompoundMessage(LANG, "realname.msg", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage FIND_FOUND = new CompoundMessage(LANG, "find.found", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage SEND_SUCCESS = new CompoundMessage(LANG, "send.success", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SEND_CONNECTED = new CompoundMessage(LANG, "send.connected", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage GLIST_SERVER = new CompoundMessage(LANG, "glist.server", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage GLIST_HOVER_STATUS = new Message(LANG, "glist.hover_status");
    public static final IMessage GLIST_HOVER_DISPLAYNAME = new Message(LANG, "glist.hover_displayname");
    public static final IMessage GLIST_TOTAL_ONLINE = new CompoundMessage(LANG, "glist.total_online", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage GTP_TP_TO_SELF = new CompoundMessage(LANG, "gtp.tp_to_self", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage LOOKUP_HEADER = new CompoundMessage(LANG, "lookup.header", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage LOOKUP_PREFIX = new Message(LANG, "lookup.prefix");
    public static final IMessage LOOKUP_USERNAME = new CompoundMessage(LANG, "lookup.username", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_DISPLAYNAME = new CompoundMessage(LANG, "lookup.displayname", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_UUID = new CompoundMessage(LANG, "lookup.uuid", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_UUID_HOVER = new Message(LANG, "lookup.uuid_hover");
    public static final IMessage LOOKUP_DISCORD = new CompoundMessage(LANG, "lookup.discord", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_DISCORD_HOVER = new Message(LANG, "lookup.discord_hover");
    public static final IMessage LOOKUP_STATUS = new CompoundMessage(LANG, "lookup.status", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_LANG = new CompoundMessage(LANG, "lookup.lang", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_VERSION = new CompoundMessage(LANG, "lookup.version", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_IP = new CompoundMessage(LANG, "lookup.ip", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_ALTS = new CompoundMessage(LANG, "lookup.alts", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_ALTS_HOVER = new Message(LANG, "lookup.alts.hover");
    public static final IMessage LOOKUP_JOINED = new CompoundMessage(LANG, "lookup.joined", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_LAST_LOGIN = new CompoundMessage(LANG, "lookup.last_login", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_LAST_LOGOUT = new CompoundMessage(LANG, "lookup.last_logout", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_PLAYTIME = new CompoundMessage(LANG, "lookup.playtime", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_RANK = new CompoundMessage(LANG, "lookup.rank", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_BANNED = new CompoundMessage(LANG, "lookup.banned", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));
    public static final IMessage LOOKUP_MUTED = new CompoundMessage(LANG, "lookup.muted", new MessagePlaceholder("%prefix%", LOOKUP_PREFIX));

    public static final IMessage SERVER_OFFLINE = new CompoundMessage(LANG, "server.offline", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVER_CONNECTED = new CompoundMessage(LANG, "server.connected", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVER_ALREADY_CONNECTED = new CompoundMessage(LANG, "server.already_connected", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage LOBBY_NOT_FOUND = new CompoundMessage(LANG, "lobby.not_found", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage LOBBY_ALREADY_IN = new CompoundMessage(LANG, "lobby.already_in", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage MESSAGE_FORMAT = new CompoundMessage(LANG, "message.format", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage MESSAGE_IGNORED = new CompoundMessage(LANG, "message.ignored", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage MESSAGE_YOU_IGNORED = new CompoundMessage(LANG, "message.you_ignored", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPLY_NO_TARGET = new CompoundMessage(LANG, "reply.no_target", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage SOCIALSPY_SUCCESS = new CompoundMessage(LANG, "socialspy.success", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SOCIALSPY_FORMAT = new CompoundMessage(LANG, "socialspy.format", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage IGNORE_ALREADY_IGNORED = new CompoundMessage(LANG, "ignore.already_ignore", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage IGNORE_EXEMPT = new CompoundMessage(LANG, "ignore.exempt", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage IGNORE_IGNORED = new CompoundMessage(LANG, "ignore.ignored", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNIGNORE_NOT_IGNORED = new CompoundMessage(LANG, "unignore.not_ignored", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNIGNORE_UNIGNORED = new CompoundMessage(LANG, "unignore.unignored", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage CLEARCHAT_MESSAGE = new CompoundMessage(LANG, "clearchat.message", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CLEARCHAT_MESSAGE_CLEARER = new CompoundMessage(LANG, "clearchat.message_clearer", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage CHATLOCK_LOCK = new CompoundMessage(LANG, "chatlock.lock", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHATLOCK_UNLOCK = new CompoundMessage(LANG, "chatlock.unlock", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHATLOCK_CHAT_IS_LOCKED = new CompoundMessage(LANG, "chatlock.chat_is_locked", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHATLOCK_YOU_LOCKED = new CompoundMessage(LANG, "chatlock.you_locked", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHATLOCK_YOU_UNLOCKED = new CompoundMessage(LANG, "chatlock.you_unlocked", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHATLOCK_STATUS_GLOBAL = new CompoundMessage(LANG, "chatlock.status.global", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHATLOCK_STATUS_SERVER = new CompoundMessage(LANG, "chatlock.status.server", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage ANNOUNCE_CHAT_FORMAT = new CompoundMessage(LANG, "announce.chat.format", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage ANNOUNCE_SENT = new CompoundMessage(LANG, "announce.sent", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage MAINTENANCE_ALREADY_SET = new CompoundMessage(LANG, "maintenance.already_set", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage MAINTENANCE_STATUS = new CompoundMessage(LANG, "maintenance.status", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage MAINTENANCE_KICK = new Message(LANG, "maintenance.kick");
    public static final IMessage MAINTENANCE_BROADCAST = new CompoundMessage(LANG, "maintenance.broadcast", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage PING_NO_CONSOLE = new CompoundMessage(LANG, "ping.no_console", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PING_SELF = new CompoundMessage(LANG, "ping.self", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PING_OTHER = new CompoundMessage(LANG, "ping.other", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage SEEN_ERROR = new CompoundMessage(LANG, "seen.error", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SEEN_STATUS = new CompoundMessage(LANG, "seen.status", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage PLAYTIME_NO_CONSOLE = new CompoundMessage(LANG, "playtime.no_console", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PLAYTIME_SELF = new CompoundMessage(LANG, "playtime.self", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PLAYTIME_OTHER = new CompoundMessage(LANG, "playtime.other", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PLAYTIME_TOP = new CompoundMessage(LANG, "playtime.top", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage HELPOP_SENT = new CompoundMessage(LANG, "helpop.sent", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_NEW = new CompoundMessage(LANG, "helpops.new", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_OPEN = new CompoundMessage(LANG, "helpops.open", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_OPEN_HOVER = new CompoundMessage(LANG, "helpops.open.hover", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_ACCEPTED = new CompoundMessage(LANG, "helpops.accepted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_ACCEPTED_HOVER = new CompoundMessage(LANG, "helpops.accepted.hover", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage REPORT_SENT = new CompoundMessage(LANG, "report.sent", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORT_EXEMPT = new CompoundMessage(LANG, "report.exempt", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_NEW = new CompoundMessage(LANG, "reports.new", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_OPEN = new CompoundMessage(LANG, "reports.open", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_OPEN_HOVER = new CompoundMessage(LANG, "reports.open.hover", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_ACCEPTED = new CompoundMessage(LANG, "reports.accepted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_ACCEPTED_HOVER = new CompoundMessage(LANG, "reports.accepted.hover", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage BUG_SENT = new CompoundMessage(LANG, "bug.sent", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_NEW = new CompoundMessage(LANG, "bugs.new", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_OPEN = new CompoundMessage(LANG, "bugs.open", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_OPEN_HOVER = new CompoundMessage(LANG, "bugs.open.hover", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_ACCEPTED = new CompoundMessage(LANG, "bugs.accepted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_ACCEPTED_HOVER = new CompoundMessage(LANG, "bugs.accepted.hover", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_ADD_MESSAGE = new CompoundMessage(LANG, "bugs.add_message", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_TYPE_MESSAGE = new CompoundMessage(LANG, "bugs.type_message", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage SERVERMANAGER_SERVER_ALREADY_EXISTS = new CompoundMessage(LANG, "servermanager.server_already_exists", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_GROUP_ALREADY_EXISTS = new CompoundMessage(LANG, "servermanager.group_already_exists", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_ALREADY_IN_GROUP = new CompoundMessage(LANG, "servermanager.server_already_in_group", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_NOT_IN_GROUP = new CompoundMessage(LANG, "servermanager.server_not_in_group", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage SERVERMANAGER_RELOADED = new CompoundMessage(LANG, "servermanager.reloaded", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_LIST_FORMAT = new CompoundMessage(LANG, "servermanager.server.list.format", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_LIST_FORMAT_DISPLAYNAME = new CompoundMessage(LANG, "servermanager.server.list.format.displayname", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_INFO_FORMAT = new Message(LANG, "servermanager.server.info.format");
    public static final IMessage SERVERMANAGER_SERVER_ADDED = new CompoundMessage(LANG, "servermanager.server_added", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_REMOVED = new CompoundMessage(LANG, "servermanager.server_removed", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_EDITED_NAME = new CompoundMessage(LANG, "servermanager.server_edited.name", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_EDITED_DISPLAYNAME = new CompoundMessage(LANG, "servermanager.server_edited.displayname", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_EDITED_IP = new CompoundMessage(LANG, "servermanager.server_edited.ip", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_EDITED_PORT = new CompoundMessage(LANG, "servermanager.server_edited.port", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_GROUP_LIST_FORMAT = new CompoundMessage(LANG, "servermanager.group.list.format", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_GROUP_ADDED = new CompoundMessage(LANG, "servermanager.group_added", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_GROUP_REMOVED = new CompoundMessage(LANG, "servermanager.group_removed", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_ADDED_TO_GROUP = new CompoundMessage(LANG, "servermanager.server_added_to_group", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SERVERMANAGER_SERVER_REMOVED_FROM_GROUP = new CompoundMessage(LANG, "servermanager.server_removed_from_group", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage DISCORD_INFO = new CompoundMessage(LANG, "discord.info", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REGISTER_SUCCESS = new CompoundMessage(LANG, "register.success", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REGISTER_DISCORD_ALREADY_REGISTERED = new Message(LANG, "register.discord.already_registered");
    public static final IMessage REGISTER_DISCORD_SUCCESS_TITLE = new Message(LANG, "register.discord.success.title");
    public static final IMessage REGISTER_DISCORD_SUCCESS_DESC = new Message(LANG, "register.discord.success.desc");
    public static final IMessage UNREGISTER_CONFIRM = new CompoundMessage(LANG, "unregister.confirm", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNREGISTER_CONFIRM_OTHER = new CompoundMessage(LANG, "unregister.confirm.other", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNREGISTER_NOT_REGISTERED = new CompoundMessage(LANG, "unregister.not_registered", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNREGISTER_NOT_REGISTERED_OTHER = new CompoundMessage(LANG, "unregister.not_registered.other", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNREGISTER_SUCCESS = new CompoundMessage(LANG, "unregister.success", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNREGISTER_SUCCESS_OTHER = new CompoundMessage(LANG, "unregister.success.other", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNREGISTER_SUCCESS_BY_ADMIN = new CompoundMessage(LANG, "unregister.success_by_admin", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage UNREGISTER_DISCORD_SUCCESS = new Message(LANG, "unregister.discord.success");

    private CommandMessages() {
    }
}
