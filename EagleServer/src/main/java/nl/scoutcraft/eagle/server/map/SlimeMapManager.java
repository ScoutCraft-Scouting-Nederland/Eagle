package nl.scoutcraft.eagle.server.map;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.SlimeException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import nl.scoutcraft.eagle.libs.sql.ISQLFunction;
import nl.scoutcraft.eagle.server.EagleServer;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SlimeMapManager implements IMapManager {

    private static final SlimePropertyMap PROPERTIES = new SlimePropertyMap();

    private final JavaPlugin plugin;
    private final SlimePlugin slimePlugin;

    public SlimeMapManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    }

    @Override
    public List<String> getMapNames(String query, ISQLFunction<ResultSet, String> transformer) {
        List<String> names = new ArrayList<>();

        try (Connection conn = EagleServer.getInstance().getSQLManager().getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                names.add(transformer.apply(rs));
        } catch (SQLException exc) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to load map names!", exc);
        }

        return names;
    }

    @Override
    @Nullable
    public <T extends IMap> T getMap(String query, ISQLFunction<ResultSet, T> transformer) {
        T map = null;
        try (Connection conn = EagleServer.getInstance().getSQLManager().getDefaultDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            map = transformer.apply(rs);
        } catch (SQLException exc) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to get map info!", exc);
        }

        return map;
    }

    @Override
    public <T extends IMap> void loadMap(T map) {
        SlimeLoader loader = this.slimePlugin.getLoader(this.plugin.getConfig().getString("swm-loader"));

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                SlimeWorld world = this.slimePlugin.loadWorld(loader, map.getSlimeName(), true, PROPERTIES);
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    this.slimePlugin.generateWorld(world);
                    this.setupGamerules(Bukkit.getWorld(world.getName()), map.getTimeTicks());
                });
            } catch (SlimeException | IOException exc) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to load map: ", exc);
            }
        });
    }

    private void setupGamerules(@Nullable World w, long timeTicks) {
        if (w == null)
            return;

        w.setTime(timeTicks);
        w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        w.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true);
        w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        w.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        w.setGameRule(GameRule.DO_FIRE_TICK, false);
        w.setGameRule(GameRule.DO_MOB_LOOT, false);
        w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        w.setGameRule(GameRule.DO_TILE_DROPS, false);
        w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        w.setGameRule(GameRule.MOB_GRIEFING, false);
        w.setGameRule(GameRule.NATURAL_REGENERATION, false);
        w.setGameRule(GameRule.DISABLE_RAIDS, true);
        w.setGameRule(GameRule.DO_INSOMNIA, false);
        w.setGameRule(GameRule.DROWNING_DAMAGE, false);
        w.setGameRule(GameRule.FALL_DAMAGE, false);
        w.setGameRule(GameRule.FIRE_DAMAGE, false);
        w.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
        w.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
        w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        w.setGameRule(GameRule.SPAWN_RADIUS, 0);
    }

    static {
        PROPERTIES.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        PROPERTIES.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        PROPERTIES.setValue(SlimeProperties.PVP, true);
        PROPERTIES.setValue(SlimeProperties.DIFFICULTY, "peaceful");
        PROPERTIES.setValue(SlimeProperties.WORLD_TYPE, "DEFAULT");
        PROPERTIES.setValue(SlimeProperties.ENVIRONMENT, "NORMAL");
    }
}
