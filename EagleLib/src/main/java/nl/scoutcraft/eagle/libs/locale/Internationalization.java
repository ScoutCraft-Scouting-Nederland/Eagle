package nl.scoutcraft.eagle.libs.locale;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Internationalization {

    private static final Logger LOGGER = Logger.getLogger("Lang");

    private final String name;
    private final Path langDir;
    private final Locale defaultLocale;
    private final ClassLoader defaultClassLoader;
    private final Map<Locale, ResourceBundle> bundles;

    public Internationalization(String name, Path langDir, Locale defaultLocale, Function<String, InputStream> resourceGetter, List<Locale> defaultLangFiles) {
        this.name = name;
        this.langDir = langDir;
        this.defaultLocale = defaultLocale;
        this.defaultClassLoader = null;
        this.bundles = new HashMap<>();

        this.saveDefaultLangFiles(defaultLangFiles, resourceGetter);
    }

    public String getString(@Nullable Locale locale, String key) {
        return this.getBundle(locale).getString(key);
    }

/*
    public Component getComponent(@Nullable Locale locale, String key, boolean colorized) {
        return colorized ? TextUtils.colorize(this.getString(locale, key)) : Component.text(this.getString(locale, key));
    }
*/

    public Enumeration<String> getKeys() {
        return this.getBundle(this.defaultLocale).getKeys();
    }

    public ResourceBundle getBundle(@Nullable Locale locale) {
        return this.bundles.computeIfAbsent(locale != null ? locale : this.defaultLocale, this::loadBundle);
    }

    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }

    public Set<Locale> getLocales() {
        return this.bundles.keySet();
    }

    @Nullable
    private ResourceBundle loadBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle(this.name, locale, new URLClassLoader(new URL[]{this.langDir.toUri().toURL()}));
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, "Failed to load resource bundle for " + locale.toLanguageTag() + ": using default file", exc);
            return this.getDefaultBundle(locale);
        }
    }

    @Nullable
    private ResourceBundle getDefaultBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle("lang/" + this.name, locale, this.defaultClassLoader);
        } catch (Exception exc) {
            LOGGER.log(Level.SEVERE, "No default lang file found for " + locale.toLanguageTag());
            return null;
        }
    }

    private void saveDefaultLangFiles(List<Locale> defaultLangFiles, Function<String, InputStream> resourceGetter) {
        try {
            if (!Files.exists(this.langDir))
                Files.createDirectories(this.langDir);

            for (Locale locale : defaultLangFiles) {
                String fileName = this.name + "_" + locale.toLanguageTag() + ".properties";
                Path target = this.langDir.resolve(fileName);

                if (!Files.exists(target)) {
                    InputStream resource = resourceGetter.apply("lang/" + fileName);
                    if (resource != null)
                        Files.copy(resource, target);
                    else LOGGER.warning("Failed to locate resource lang/" + fileName + ", skipping...");
                }
            }
        } catch (IOException exc) {
            LOGGER.log(Level.SEVERE, "Failed to save default lang files!", exc);
        }
    }

    public void reload() {
        List<Locale> langs = new ArrayList<>(this.bundles.keySet());
        this.bundles.clear();
        langs.forEach(this::loadBundle);
    }

    @Deprecated
    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static Builder builder(String name, Function<String, InputStream> resourceGetter) {
        return new Builder(name, resourceGetter);
    }

    public static class Builder {

        private final String name;
        private Path langDir;
        private Locale defaultLocale;

        private Function<String, InputStream> resourceGetter;
        private final List<Locale> defaultLangFiles;

        @Deprecated
        private Builder(String name) {
            this(name, null);
        }

        private Builder(String name, Function<String, InputStream> resourceGetter) {
            this.name = name;
            this.resourceGetter = resourceGetter;
            this.defaultLangFiles = new ArrayList<>();
        }

        public Builder setLangDir(Path langDir) {
            this.langDir = langDir;
            return this;
        }

        public Builder setDefaultLocale(String tag) {
            return this.setDefaultLocale(Locale.forLanguageTag(tag));
        }

        public Builder setDefaultLocale(@Nullable Locale locale) {
            this.defaultLocale = locale;
            return this;
        }

        @Deprecated
        public Builder setDefaultClassLoader(ClassLoader classLoader) {
            this.resourceGetter = classLoader::getResourceAsStream;
            return this;
        }

        public Builder addDefaultLangFiles(String... tags) {
            for (String tag : tags)
                this.addDefaultLangFile(tag);
            return this;
        }

        public Builder addDefaultLangFile(String tag) {
            return this.addDefaultLangFile(Locale.forLanguageTag(tag));
        }

        public Builder addDefaultLangFile(Locale locale) {
            this.defaultLangFiles.add(locale);
            return this;
        }

        public Internationalization build() {
            if (this.name == null || this.langDir == null || this.resourceGetter == null)
                throw new IllegalArgumentException("A name, lang directory and resource getter are required to build an Internationalization instance");

            if (this.defaultLocale == null)
                this.defaultLocale = Locale.forLanguageTag("nl");

            return new Internationalization(this.name, this.langDir, this.defaultLocale, this.resourceGetter, this.defaultLangFiles);
        }
    }
}
