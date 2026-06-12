package org.whilmarbitoco.core.database;

import org.whilmarbitoco.core.utils.Config;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Database connection manager with a built-in lightweight connection pool.
 *
 * No external pool library required. Uses DriverManager internally with a
 * simple blocking queue pool. Detects the JDBC driver automatically from the
 * classpath (JDBC 4.0+ SPI), so whichever driver you include (MySQL,
 * PostgreSQL, SQLite) is picked up without configuration.
 *
 * Pool behavior:
 * - Connections are reused from the pool when available.
 * - When the pool is empty but the max size has not been reached, a new connection is created.
 * - When the max size is reached, getConnection() blocks until one is returned.
 * - Dead connections (closed or invalid) are discarded and replaced.
 * - Connections returned to a full pool are closed immediately (no leak).
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
    private static final BlockingQueue<Connection> pool;
    private static final String url;
    private static final String user;
    private static final String password;
    private static final int poolSize;
    private static final AtomicInteger activeCount = new AtomicInteger(0);

    static {
        url = Config.dbUrl();
        user = Config.dbUser();
        password = Config.dbPassword();
        int configuredSize = Config.poolSize();
        poolSize = configuredSize > 0 ? configuredSize : DEFAULT_POOL_SIZE;
        pool = new ArrayBlockingQueue<>(poolSize);
    }

    /**
     * Get a connection from the pool. Blocks if the pool is at max capacity
     * and no connections are available.
     */
    public static Connection getConnection() {
        while (true) {
            Connection conn = pool.poll();
            if (conn != null) {
                try {
                    if (!conn.isClosed() && conn.isValid(2)) {
                        activeCount.incrementAndGet();
                        return wrap(conn);
                    }
                } catch (SQLException e) {
                    // Connection is dead, try again
                }
                continue;
            }

            // Pool is empty — check if we can create a new one
            int current = activeCount.get();
            if (current < poolSize) {
                if (activeCount.compareAndSet(current, current + 1)) {
                    return wrap(createConnection());
                }
                Thread.yield();
                continue;
            }

            // Pool exhausted — block until a connection is returned
            try {
                Connection returned = pool.take();
                if (returned != null) {
                    try {
                        if (!returned.isClosed() && returned.isValid(2)) {
                            activeCount.incrementAndGet();
                            return wrap(returned);
                        }
                    } catch (SQLException e) {
                        // Dead, loop again
                    }
                    activeCount.decrementAndGet();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for connection", e);
            }
        }
    }

    /**
     * Return a connection to the pool. Called automatically when the wrapped
     * Connection's close() method is invoked.
     */
    private static void returnConnection(Connection real) {
        activeCount.decrementAndGet();
        if (real == null) return;
        try {
            if (!real.isClosed() && real.isValid(2)) {
                if (!pool.offer(real)) {
                    // Pool is full — close to prevent leak
                    real.close();
                }
            }
        } catch (SQLException e) {
            // Connection is dead
        }
    }

    /**
     * Wrap a real Connection so that close() returns it to the pool
     * instead of actually closing it.
     */
    private static Connection wrap(Connection real) {
        return (Connection) Proxy.newProxyInstance(
                real.getClass().getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        returnConnection(real);
                        return null;
                    }
                    return method.invoke(real, args);
                }
        );
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
        activeCount.set(0);
    }
}
