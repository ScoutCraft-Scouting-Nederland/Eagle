package nl.scoutcraft.eagle.limbo;

import com.loohp.limbo.plugins.LimboPlugin;
import nl.scoutcraft.eagle.libs.Eagle;
import nl.scoutcraft.eagle.libs.IEagle;
import nl.scoutcraft.eagle.libs.sql.ISQLManager;
import nl.scoutcraft.eagle.libs.sql.SQLManager;
import nl.scoutcraft.eagle.limbo.server.HeartbeatTask;

public final class EagleLimbo extends LimboPlugin implements IEagle {

    private static EagleLimbo instance;

    private ISQLManager sqlManager;

    @Override
    public void onLoad() {
        instance = this;
        Eagle.init(this);
    }

    @Override
    public void onEnable() {
        this.sqlManager = new SQLManager("IP", 3306, "scoutcraft_eagle", "scoutcraft_eagle", "PASSWORD");

        new HeartbeatTask().start(this);
    }

    @Override
    public void onDisable() {
        this.sqlManager.close();
        this.getServer().getScheduler().cancelTask(this);
    }

    @Override
    public ISQLManager getSQLManager() {
        return this.sqlManager;
    }

    public static EagleLimbo getInstance() {
        return instance;
    }
}
