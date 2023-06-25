package nl.scoutcraft.eagle.server.locale;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.server.EagleServer;

public final class Messages {

    private static final Internationalization LANG = EagleServer.getInstance().getLang();

    public static final IMessage PARTY_PREFIX = new Message(LANG, "party.prefix");
    public static final IMessage PARTY_ENTER_NAME = new CompoundMessage(LANG, "party.enter_name", new MessagePlaceholder("%prefix%", PARTY_PREFIX));
    public static final IMessage PARTY_JOIN_PUBLIC = new CompoundMessage(LANG, "party.join_public", new MessagePlaceholder("%prefix%", PARTY_PREFIX));
}
