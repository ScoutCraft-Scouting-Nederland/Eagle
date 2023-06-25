package nl.scoutcraft.eagle.scotty;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import nl.scoutcraft.eagle.scotty.config.ConfigAdaptor;
import nl.scoutcraft.eagle.scotty.discord.DiscordManager;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "scotty",
        name = "Scotty",
        version = "1.0.0",
        description = "The discord bot connection for Eagle",
        dependencies = { @Dependency(id = "luckperms")},
        authors = { "__Daniel" },
        url = "https://minecraft.scouting.nl")
public class Scotty {

    private static ProxyServer PROXY;
    private static Scotty INSTANCE;

    private final Logger logger;
    private final ConfigAdaptor config;
    private final DiscordManager discordManager;

    @Inject
    public Scotty(ProxyServer proxy, Logger logger, @DataDirectory Path configDir) {
        PROXY = proxy;
        INSTANCE = this;

        this.logger = logger;
        this.config = new ConfigAdaptor(this, logger, configDir);
        this.discordManager = new DiscordManager(this);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        if (!this.config.setup() || !this.config.getNode("enabled").getBoolean(false))
            return;

        this.logger.info("Enabled Scotty successfully!");

        PROXY.getScheduler()
                .buildTask(this, this.discordManager::start)
                .delay(50L, TimeUnit.MILLISECONDS)
                .schedule();
    }

    @Subscribe
    public void onProxyReload(ProxyReloadEvent event) {
        this.discordManager.close();

        this.onProxyInitialization(null);
    }

    public Logger getLogger() {
        return this.logger;
    }

    public ConfigAdaptor getConfig() {
        return this.config;
    }

    public DiscordManager getDiscordManager() {
        return this.discordManager;
    }

    public static Scotty getInstance() {
        return INSTANCE;
    }

    public static ProxyServer getProxy() {
        return PROXY;
    }
}
