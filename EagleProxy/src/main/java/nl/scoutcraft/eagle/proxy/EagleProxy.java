package nl.scoutcraft.eagle.proxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import nl.scoutcraft.eagle.libs.Eagle;
import nl.scoutcraft.eagle.libs.IEagle;
import nl.scoutcraft.eagle.proxy.chat.ChatManager;
import nl.scoutcraft.eagle.proxy.commands.CommandRegistry;
import nl.scoutcraft.eagle.proxy.discord.IDiscordManager;
import nl.scoutcraft.eagle.proxy.event.ConfigReloadEvent;
import nl.scoutcraft.eagle.proxy.io.ConfigAdaptor;
import nl.scoutcraft.eagle.proxy.io.DatabaseManager;
import nl.scoutcraft.eagle.proxy.locale.LangManager;
import nl.scoutcraft.eagle.proxy.party.PartyManager;
import nl.scoutcraft.eagle.proxy.player.PlayerNodeChangeListener;
import nl.scoutcraft.eagle.proxy.player.obj.PlayerManager;
import nl.scoutcraft.eagle.proxy.server.ProxyManager;
import nl.scoutcraft.eagle.proxy.server.ServerManager;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "eagleproxy",
        name = "EagleProxy",
        version = "2.0.0",
        description = "EagleProxy contains all the main methods for ScoutCraft",
        dependencies = { @Dependency(id = "Scotty", optional = true) },
        authors = {"Acixsi", "Daniel12321"},
        url = "https://scoutcraft.nl")
public final class EagleProxy implements IEagle {

    private static EagleProxy INSTANCE;
    private static ProxyServer PROXY;

    private final Logger logger;
    private final Path dataDirectory;

    private ConfigAdaptor config;
    private DatabaseManager sqlManager;
    private LangManager langManager;
    private ProxyManager proxyManager;
    private ServerManager serverManager;
    private PlayerManager playerManager;
    private ChatManager chatManager;
    private PartyManager partyManager;
    private IDiscordManager discordManager;

    @Inject
    public EagleProxy(ProxyServer proxy, Logger logger, @DataDirectory Path dataDirectory) {
        INSTANCE = this;
        PROXY = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        Eagle.init(this);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.logger.info("Loading eagle...");

        this.config = new ConfigAdaptor(this, this.dataDirectory);
        if (!this.config.setup())
            return;

        this.sqlManager = new DatabaseManager(this, this.config);
        this.langManager = new LangManager(this);
        this.proxyManager = new ProxyManager(this);
        this.serverManager = new ServerManager(this);
        this.playerManager = new PlayerManager(this);
        this.chatManager = new ChatManager(this);
        this.partyManager = new PartyManager(this);
        this.discordManager = IDiscordManager.of(PROXY, this.config.getNode("discord", "enabled").getBoolean(false));

        this.serverManager.load();
        this.chatManager.load(this.config);

        this.registerListeners();
        this.registerCommands();

        this.logger.info("Loaded eagle successfully!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.sqlManager.close();
        this.discordManager.close();
    }

    public void reload() {
        if (!this.config.setup())
            return;

        this.getLogger().info("Loaded config.yml!");

        this.langManager.reload();
        this.serverManager.load();

        PROXY.getEventManager().fireAndForget(new ConfigReloadEvent(this.config));
    }

    private void registerListeners() {
        EventManager em = PROXY.getEventManager();
        em.register(this, this.proxyManager);
        em.register(this, this.playerManager);
        em.register(this, this.chatManager);
        em.register(this, this.serverManager.getLoadBalancer());
        em.register(this, this.discordManager);
        new PlayerNodeChangeListener(this).register();
    }

    private void registerCommands() {
        CommandRegistry.register(PROXY.getCommandManager(), this);
    }

    public ConfigAdaptor getConfigAdaptor() {
        return this.config;
    }

    @Override
    public DatabaseManager getSQLManager() {
        return this.sqlManager;
    }

    public LangManager getLangManager() {
        return this.langManager;
    }

    public ProxyManager getProxyManager() {
        return this.proxyManager;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public ChatManager getChatManager() {
        return this.chatManager;
    }

    public PartyManager getPartyManager() {
        return this.partyManager;
    }

    public IDiscordManager getDiscordManager() {
        return this.discordManager;
    }

    public static EagleProxy getInstance() {
        return INSTANCE;
    }

    public static ProxyServer getProxy() {
        return PROXY;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }
}
