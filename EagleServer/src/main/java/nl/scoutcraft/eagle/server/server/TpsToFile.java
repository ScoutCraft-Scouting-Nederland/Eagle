package nl.scoutcraft.eagle.server.server;

import nl.scoutcraft.eagle.server.EagleServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class TpsToFile implements Runnable {

    private final File file;
    private final FileConfiguration config;

    public TpsToFile() {
        this.file = new File(EagleServer.getInstance().getDataFolder(), "ttf.yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void start(EagleServer plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 20L, 20L);
    }

    @Override
    public void run() {
        try {
            this.config.set("tps", Bukkit.getTPS());
            this.config.save(this.file);
        } catch (IOException exc) {
            EagleServer.getInstance().getLogger().log(Level.SEVERE, "Failed to write TPS to file!", exc);
        }
    }
}
