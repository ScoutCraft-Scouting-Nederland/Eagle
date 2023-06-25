package nl.scoutcraft.eagle.proxy.locale;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.proxy.EagleProxy;

import java.io.InputStream;
import java.util.Locale;
import java.util.function.Function;

public class LangManager {

    private final Internationalization generalLang;
    private final Internationalization partyLang;
    private final Internationalization commandLang;
    private final Internationalization announcementLang;

    public LangManager(EagleProxy plugin) {
        plugin.getLogger().info("Loading languages...");

        final Function<String, InputStream> resourceGetter = LangManager.class::getResourceAsStream;

        this.generalLang = Internationalization.builder("general", resourceGetter)
                .setLangDir(plugin.getDataDirectory().resolve("lang"))
                .addDefaultLangFiles("nl", "en")
                .build();
        this.partyLang = Internationalization.builder("party", resourceGetter)
                .setLangDir(plugin.getDataDirectory().resolve("lang"))
                .addDefaultLangFiles("nl", "en")
                .build();
        this.commandLang = Internationalization.builder("commands", resourceGetter)
                .setLangDir(plugin.getDataDirectory().resolve("lang"))
                .addDefaultLangFiles("nl", "en")
                .build();
        this.announcementLang = Internationalization.builder("announcements", resourceGetter)
                .setLangDir(plugin.getDataDirectory().resolve("lang"))
                .addDefaultLangFiles("nl", "en")
                .build();

        // Preload both bundles to allow the lang command to find usable locale's
        this.commandLang.getBundle(Locale.forLanguageTag("nl"));
        this.commandLang.getBundle(Locale.forLanguageTag("en"));

        plugin.getLogger().info("Loaded languages successfully!");
    }

    public void reload() {
        this.generalLang.reload();
        this.partyLang.reload();
        this.commandLang.reload();
        this.announcementLang.reload();
    }

    public Internationalization getGeneralLang() {
        return this.generalLang;
    }

    public Internationalization getPartyLang() {
        return this.partyLang;
    }

    public Internationalization getCommandLang() {
        return this.commandLang;
    }

    public Internationalization getAnnouncementLang() {
        return this.announcementLang;
    }
}
