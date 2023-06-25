package nl.scoutcraft.eagle.server.data;

import nl.scoutcraft.eagle.server.EagleServer;
import org.bukkit.NamespacedKey;

public final class EagleKeys {

    public static final LocaleTagType LOCALE_TAG_TYPE = new LocaleTagType();
    public static final UUIDListTagType UUID_LIST_TAG_TYPE = new UUIDListTagType();

    public static final NamespacedKey BUTTON_ID = new NamespacedKey(EagleServer.getInstance(), "button_id");
    public static final NamespacedKey BUTTON_IMMOVABLE = new NamespacedKey(EagleServer.getInstance(), "button_immovable");
    public static final NamespacedKey LOCALE = new NamespacedKey(EagleServer.getInstance(), "locale");
    public static final NamespacedKey PREFIX = new NamespacedKey(EagleServer.getInstance(), "prefix");
    public static final NamespacedKey DISPLAY_NAME = new NamespacedKey(EagleServer.getInstance(), "display_name");
    public static final NamespacedKey IGNORED_BY = new NamespacedKey(EagleServer.getInstance(), "ignored_by");

    private EagleKeys(){}
}
