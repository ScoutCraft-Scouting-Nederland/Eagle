package nl.scoutcraft.eagle.libs.sql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabase {

    /**
     * Gets a {@link Connection} from the {@link HikariDataSource}.
     * Do not forget to close the {@link Connection} after use.
     *
     * @return The SQL {@link Connection}.
     */
    Connection getConnection() throws SQLException;

    /**
     * Closes the {@link HikariDataSource}.
     */
    void close();

    /**
     * Executes an update statement. Useful for setting up tables.
     *
     * @param sql The sql query.
     */
    void executeUpdate(String sql);
}
