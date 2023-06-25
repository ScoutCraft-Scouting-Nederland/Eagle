package nl.scoutcraft.eagle.proxy.locale;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.api.CompoundMessage;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.Message;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;

public final class GeneralMessages {

    private static final Internationalization LANG = EagleProxy.getInstance().getLangManager().getGeneralLang();

    public static final IMessage PREFIX = new Message(LANG, "prefix");
    public static final IMessage NO_CONSOLE = new Message(LANG, "no_console");
    public static final IMessage ONLINE = new Message(LANG, "online");
    public static final IMessage OFFLINE = new Message(LANG, "offline");

    public static final IMessage CHAT_CAPS = new CompoundMessage(LANG, "chat.caps", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHAT_SPAMMING = new CompoundMessage(LANG, "chat.spamming", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHAT_LINK = new CompoundMessage(LANG, "chat.link", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage GAMESTATE_OFFLINE = new Message(LANG, "game.state_offline");
    public static final IMessage GAMESTATE_WAITING = new Message(LANG, "game.state_waiting");
    public static final IMessage GAMESTATE_FULL = new Message(LANG, "game.state_full");
    public static final IMessage GAMESTATE_INGAME = new Message(LANG, "game.state_ingame");
    public static final IMessage GAMESTATE_RESTARTING = new Message(LANG, "game.state_restarting");

    public static final IMessage SERVER_PING = new CompoundMessage(LANG, "server.ping", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage TEAMCHAT_SUBMIT_MESSAGE = new CompoundMessage(LANG, "teamchat.submit_message", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage TEAMCHAT_FORMAT = new Message(LANG, "teamchat.format");

    public static final IMessage HELPOPS_OPEN = new CompoundMessage(LANG, "helpops.open", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_OPEN_ONE = new CompoundMessage(LANG, "helpops.open.one", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_ACCEPTED = new CompoundMessage(LANG, "helpops.accepted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HELPOPS_ACCEPTED_ONE = new CompoundMessage(LANG, "helpops.accepted.one", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_OPEN = new CompoundMessage(LANG, "reports.open", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_OPEN_ONE = new CompoundMessage(LANG, "reports.open.one", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_ACCEPTED = new CompoundMessage(LANG, "reports.accepted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage REPORTS_ACCEPTED_ONE = new CompoundMessage(LANG, "reports.accepted.one", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_OPEN = new CompoundMessage(LANG, "bugs.open", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_OPEN_ONE = new CompoundMessage(LANG, "bugs.open.one", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_ACCEPTED = new CompoundMessage(LANG, "bugs.accepted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BUGS_ACCEPTED_ONE = new CompoundMessage(LANG, "bugs.accepted.one", new MessagePlaceholder("%prefix%", PREFIX));

    private GeneralMessages() {
    }
}
