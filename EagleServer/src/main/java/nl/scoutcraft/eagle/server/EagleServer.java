package nl.scoutcraft.eagle.server;

import nl.scoutcraft.eagle.libs.Eagle;
import nl.scoutcraft.eagle.libs.IEagle;
import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.libs.locale.ServerResourceGetter;
import nl.scoutcraft.eagle.libs.sql.ISQLManager;
import nl.scoutcraft.eagle.libs.sql.SQLManager;
import nl.scoutcraft.eagle.server.chat.ChatManager;
import nl.scoutcraft.eagle.server.chat.EmptyChatManager;
import nl.scoutcraft.eagle.server.chat.IChatManager;
import nl.scoutcraft.eagle.server.gui.inventory.EmptyInventoryMenuManager;
import nl.scoutcraft.eagle.server.gui.inventory.IInventoryMenuManager;
import nl.scoutcraft.eagle.server.gui.inventory.InventoryMenuManager;
import nl.scoutcraft.eagle.server.listener.ClientListener;
import nl.scoutcraft.eagle.server.listener.TablistListener;
import nl.scoutcraft.eagle.server.map.EmptyMapManager;
import nl.scoutcraft.eagle.server.map.IMapManager;
import nl.scoutcraft.eagle.server.map.SlimeMapManager;
import nl.scoutcraft.eagle.server.party.EmptyPartyManager;
import nl.scoutcraft.eagle.server.party.IPartyManager;
import nl.scoutcraft.eagle.server.party.PartyManager;
import nl.scoutcraft.eagle.server.player.PlayerManager;
import nl.scoutcraft.eagle.server.server.HeartbeatTask;
import nl.scoutcraft.eagle.server.server.ServerManager;
import nl.scoutcraft.eagle.server.server.TpsToFile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EagleServer extends JavaPlugin implements IEagle {

    private static EagleServer instance;

    private Internationalization lang;
    private PlayerManager playerManager;
    private ServerManager serverManager;
    private ISQLManager sqlManager;
    private IPartyManager partyManager;
    private IChatManager chatManager;
    private IInventoryMenuManager imm;
    private IMapManager mapManager;

    @Override
    public void onLoad() {
        instance = this;
        Eagle.init(this);
    }

    @Override
    public void onEnable() {
        super.saveDefaultConfig();
        Configuration config = super.getConfig();

        this.lang = Internationalization.builder("messages", new ServerResourceGetter(this))
                .setLangDir(super.getDataFolder().toPath().resolve("lang"))
                .addDefaultLangFile("en").addDefaultLangFile("nl")
                .build();
        this.playerManager = new PlayerManager(this);
        this.serverManager = new ServerManager(this);
        this.sqlManager = new SQLManager(config.getString("mysql.host"), config.getInt("mysql.port"), config.getString("mysql.database"), config.getString("mysql.username"), config.getString("mysql.password"));
        this.partyManager = config.getBoolean("modules.party-menus") ? new PartyManager(this) : new EmptyPartyManager();
        this.chatManager = config.getBoolean("modules.chat") ? new ChatManager(this) : new EmptyChatManager();
        this.imm = config.getBoolean("modules.guis") ? new InventoryMenuManager() : new EmptyInventoryMenuManager();
        this.mapManager = null;

        this.registerListeners();
        this.registerCommands();

        new TpsToFile().start(this);
        if (getConfig().getBoolean("modules.heartbeat")) new HeartbeatTask().start(this);
    }

    @Override
    public void onDisable() {
        this.sqlManager.close();
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ClientListener(this), this);
        pm.registerEvents(this.imm, this);
        pm.registerEvents(this.chatManager, this);
        if (getConfig().getBoolean("modules.tablist")) pm.registerEvents(new TablistListener(), this);
    }

    private void registerCommands() {
    }

    public Internationalization getLang() {
        return this.lang;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public ISQLManager getSQLManager() {
        return this.sqlManager;
    }

    public IPartyManager getPartyManager() {
        return this.partyManager;
    }

    public IChatManager getChatManager() {
        return this.chatManager;
    }

    public IInventoryMenuManager getInventoryMenuManager() {
        return this.imm;
    }

    public IMapManager getMapManager() {
        if (this.mapManager == null)
            this.mapManager = Bukkit.getPluginManager().isPluginEnabled("SlimeWorldManager") && super.getConfig().getBoolean("modules.maps") ? new SlimeMapManager(this) : new EmptyMapManager();
        return this.mapManager;
    }

    public static EagleServer getInstance() {
        return instance;
    }
}
