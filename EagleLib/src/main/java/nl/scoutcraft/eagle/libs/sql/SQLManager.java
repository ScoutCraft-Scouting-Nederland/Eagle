package nl.scoutcraft.eagle.libs.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLManager implements ISQLManager {

    private static final Logger LOGGER = Logger.getLogger("Eagle SQLManager");
    private final Map<String, IDatabase> databases;
    private final IDatabase defaultDatabase;

    public SQLManager(String host, int port, String database, String username, String password) {
        this.databases = new HashMap<>();
        this.defaultDatabase = new Database(host, port, database, username, password);
        this.databases.put(database, this.defaultDatabase);

        LOGGER.info("Prepared database connection!");
    }

    @Override
    public IDatabase getDefaultDatabase() {
        return this.defaultDatabase;
    }

    @Override
    @Nullable
    public IDatabase getDatabase(String database) {
        return this.databases.get(database);
    }

    @Override
    public IDatabase getOrCreateDatabase(String host, int port, String database, String username, String password) {
        return this.databases.computeIfAbsent(database, k -> new Database(host, port, database, username, password));
    }

    @Override
    public void close() {
        this.databases.values().forEach(IDatabase::close);
    }

    public static class Database implements IDatabase {

        private final HikariConfig config;
        @Nullable private HikariDataSource dataSource;

        Database(String host, int port, String database, String username, String password) {
            this.config = new HikariConfig();
            this.config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            this.config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            this.config.setUsername(username);
            this.config.setPassword(password);
            this.config.addDataSourceProperty("useSSL", false);
            this.config.setMaximumPoolSize(3);
            this.config.setMinimumIdle(10);
            this.config.setMaxLifetime(30000);
            this.config.setConnectionTimeout(5000);
            this.dataSource = null;
            LOGGER.info("jdbc:mysql://" + host + ":" + port + "/" + database);
        }

        public Connection getConnection() throws SQLException {
            if (this.dataSource == null || this.dataSource.isClosed()) {
                this.dataSource = new HikariDataSource(this.config);
                LOGGER.info("Opened database connection successfully!");
            }

            return this.dataSource.getConnection();
        }

        public void close() {
            if (this.dataSource != null) {
                this.dataSource.close();
                this.dataSource = null;
            }
        }

        @Override
        public void executeUpdate(String sql) {
            try (Connection conn = this.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            } catch (SQLException exc) {
                LOGGER.log(Level.SEVERE, "Failed to execute sql update", exc);
            }
        }
    }
}
