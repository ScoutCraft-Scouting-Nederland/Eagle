package nl.scoutcraft.eagle.libs.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface ISQLFunction<T, R> {

    R apply(T t) throws SQLException;
}
