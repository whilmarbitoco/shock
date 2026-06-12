package org.whilmarbitoco.core.database;

import org.whilmarbitoco.core.utils.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database connection manager using HikariCP connection pooling.
 * Dialect-aware — auto-detects the database type from the JDBC URL
 * and configures the connection pool accordingly.
 *
 * Supported databases:
 * - MySQL / MariaDB (jdbc:mysql://, jdbc:mariadb://)
 * - PostgreSQL (jdbc:postgresql://)
 * - SQLite (jdbc:sqlite:)
 *
 * Configure in config.properties:
 * <pre>
 * db.url=jdbc:mysql://localhost:3306/mydb
 * db.user=root
 * db.password=secret
 * </pre>
 *
 * To switch to PostgreSQL, just change the URL:
 * <pre>
 * db.url=jdbc:postgresql://localhost:5432/mydb
 * </pre>
 *
 * Or for SQLite:
 * <pre>
 * db.url=jdbc:sqlite:mydb.sqlite
 * </pre>
 */
public class DBConnection {

    private static final HikariDataSource dataSource;

    static {
        Dialect dialect = Dialect.fromUrl(Config.dbUrl());

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.dbUrl());
        config.setUsername(Config.dbUser());
        config.setPassword(Config.dbPassword());
        config.setDriverClassName(dialect.driverClass());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setMaxLifetime(1800000);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
}
