package nl.scoutcraft.eagle.server.map;

import nl.scoutcraft.eagle.libs.sql.ISQLFunction;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.List;

public interface IMapManager {

    /**
     * Gets a list of map names.
     *
     * @param query The SQL select query.
     * @param transformer The {@link ISQLFunction} that turn an SQL {@link ResultSet} into a String.
     * @return All map names matching the query.
     */
    List<String> getMapNames(String query, ISQLFunction<ResultSet, String> transformer);

    /**
     * Gets all info of a specific map from the database.
     *
     * @param query The SQL select query.
     * @param transformer The {@link ISQLFunction} that turn an SQL {@link ResultSet} into an {@link IMap} object.
     * @return The {@link IMap} object.
     */
    @Nullable
    <T extends IMap> T getMap(String query, ISQLFunction<ResultSet, T> transformer);

    /**
     * Loads a map using SlimeWorldManager. (ASYNC)
     *
     * @param map The {@link IMap} object.
     */
    <T extends IMap> void loadMap(T map);
}
