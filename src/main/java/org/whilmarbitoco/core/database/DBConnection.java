package org.whilmarbitoco.core.database;

import org.whilmarbitoco.core.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Database connection manager with a built-in lightweight connection pool.
 *
 * No external pool library required. Uses DriverManager internally with a
 * simple blocking queue pool. Detects the JDBC driver automatically from the
 * classpath (JDBC 4.0+ SPI), so whichever driver you include (MySQL,
 * PostgreSQL, SQLite) is picked up without configuration.
 *
 * Configure in config.properties:
 * <pre>
 * db.url=jdbc:mysql://localhost:3306/mydb
 * db.user=root
 * db.password=secret
 * db.pool.size=10
 * </pre>
 */
public class DBConnection {

    private static final int DEFAULT_POOL_SIZE = 5;
    private static BlockingQueue<Connection> pool;
    private static String url;
    private static String user;
    private static String password;
    private static int poolSize;

    static {
        url = Config.dbUrl();
        user = Config.dbUser();
        password = Config.dbPassword();
        poolSize = Config.poolSize();
        if (poolSize <= 0) poolSize = DEFAULT_POOL_SIZE;
        pool = new ArrayBlockingQueue<>(poolSize);
    }

    /**
     * Get a connection from the pool. Creates a new one if the pool is empty.
     * Callers must close the connection to return it to the pool.
     */
    public static Connection getConnection() {
        Connection conn = pool.poll();
        if (conn != null) {
            try {
                if (!conn.isClosed() && conn.isValid(2)) {
                    return conn;
                }
            } catch (SQLException e) {
                // Connection is dead, create a new one
            }
        }
        return createConnection();
    }

    /**
     * Return a connection to the pool. Called automatically when Connection.close() is invoked.
     */
    public static void returnConnection(Connection conn) {
        if (conn == null) return;
        try {
            if (!conn.isClosed() && conn.isValid(2)) {
                pool.offer(conn);
            }
        } catch (SQLException e) {
            // Connection is dead, don't return it
        }
    }

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage(), e);
        }
    }

    /**
     * Close all connections in the pool. Call on application shutdown.
     */
    public static void shutdown() {
        Connection conn;
        while ((conn = pool.poll()) != null) {
            try {
                conn.close();
            } catch (SQLException e) { /* ignore */ }
        }
    }
}
