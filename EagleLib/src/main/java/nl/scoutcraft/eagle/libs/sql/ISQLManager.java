package nl.scoutcraft.eagle.libs.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Nullable;

public interface ISQLManager {

    /**
     * Gets the default Eagle database.
     * @return The {@link IDatabase} instance
     */
    IDatabase getDefaultDatabase();

    /**
     * Gets the {@link IDatabase} instance with the given name if found.
     *
     * @param database The database name
     * @return The {@link IDatabase} instance, if found
     */
    @Nullable
    IDatabase getDatabase(String database);

    /**
     * Gets or creates a {@link IDatabase} instance for the given parameters.
     *
     * @param host The database host
     * @param port The database port
     * @param database The database name
     * @param username The database's user username
     * @param password The database's user password
     * @return The {@link IDatabase} instance
     */
    IDatabase getOrCreateDatabase(String host, int port, String database, String username, String password);

    /**
     * Closes all the {@link HikariDataSource}'s.
     */
    void close();
}
