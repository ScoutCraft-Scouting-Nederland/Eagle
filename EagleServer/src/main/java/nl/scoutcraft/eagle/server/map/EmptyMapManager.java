package nl.scoutcraft.eagle.server.map;

import nl.scoutcraft.eagle.libs.sql.ISQLFunction;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.List;

public class EmptyMapManager implements IMapManager {

    @Override
    public List<String> getMapNames(String query, ISQLFunction<ResultSet, String> transformer) {
        throw new UnsupportedOperationException("This method requires SlimeWorldManager");
    }

    @Override
    @Nullable
    public <T extends IMap> T getMap(String query, ISQLFunction<ResultSet, T> transformer) {
        throw new UnsupportedOperationException("This method requires SlimeWorldManager");
    }

    @Override
    public <T extends IMap> void loadMap(T map) {
        throw new UnsupportedOperationException("This method requires SlimeWorldManager");
    }
}
