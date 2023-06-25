package nl.scoutcraft.eagle.libs.locale;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.util.function.Function;

public class ServerResourceGetter implements Function<String, InputStream> {

    private final JavaPlugin plugin;

    public ServerResourceGetter(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public InputStream apply(String s) {
        return this.plugin.getResource(s);
    }
}
