package nl.scoutcraft.eagle.proxy.io;

import nl.scoutcraft.eagle.proxy.EagleProxy;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigAdaptor {

    private static final Logger LOGGER = EagleProxy.getInstance().getLogger();

    private final EagleProxy plugin;
    private final Path config;
    private final YamlConfigurationLoader loader;

    private ConfigurationNode node;

    public ConfigAdaptor(EagleProxy plugin, Path path) {
        this.plugin = plugin;
        this.config = path.resolve("config.yml");
        this.loader = YamlConfigurationLoader.builder().path(this.config).build();
        this.node = null;
    }

    public boolean setup() {
        try {
            Files.createDirectories(this.config.getParent());
        } catch (IOException exc) {
            LOGGER.error("Failed to create config directory!", exc);
            return false;
        }

        if (Files.notExists(this.config)) {
            try (InputStream is = this.plugin.getClass().getResourceAsStream("/config.yml")) {
                if (is == null)
                    throw new IOException("config.yml resource not found!");

                Files.copy(is, this.config);
            } catch (IOException exc) {
                LOGGER.error("Failed to create config file!", exc);
                return false;
            }
        }

        return this.load();
    }

    public boolean load() {
        try {
            this.node = this.loader.load();
        } catch (IOException ex) {
            LOGGER.warn("There was an issue while attempting to load the config", ex);
            return false;
        }

        return true;
    }

    public ConfigurationNode getNode(Object... path) {
        return this.node.node(path);
    }
}