package nl.scoutcraft.eagle.proxy.io;

import nl.scoutcraft.eagle.libs.player.PlayerInfo;
import nl.scoutcraft.eagle.libs.sql.SQLCache;
import nl.scoutcraft.eagle.proxy.EagleProxy;

import java.util.concurrent.TimeUnit;

public class PlayerInfoCache extends SQLCache<PlayerInfo> {

    public PlayerInfoCache() {
        super(10, TimeUnit.MINUTES);
    }

    @Override
    public void scheduleCleaner(Runnable runnable) {
        EagleProxy.getProxy().getScheduler()
                .buildTask(EagleProxy.getInstance(), runnable)
                .delay(5L, TimeUnit.MINUTES)
                .repeat(5L, TimeUnit.MINUTES)
                .schedule();
    }
}
