package org.whilmarbitoco.core.database;

import java.util.Map;

/**
 * Database dialect abstraction.
 * Each supported database (MySQL, PostgreSQL, SQLite, etc.) provides
 * an implementation that handles SQL generation differences.
 *
 * Usage:
 * <pre>
 *   Dialect dialect = Dialect.fromUrl(config.dbUrl());
 *   String sql = dialect.createTable(usersEntity);
 * </pre>
 */
public interface Dialect {

    // ── Identity / Auto-increment ────────────────────────────────

    /** Returns the SQL fragment for auto-incrementing primary key columns. */
    String autoIncrement();

    /** Returns the SQL type for auto-incrementing integer PKs. */
    String integerPKType();

    /** Returns the SQL type for auto-incrementing long PKs. */
    String longPKType();

    // ── Type mapping ─────────────────────────────────────────────

    /** Maps a Java type to the SQL column type for this dialect. */
    String resolveType(Class<?> javaType);

    /** Returns the full type map for this dialect. */
    Map<Class<?>, String> typeMap();

    // ── Identifier quoting ───────────────────────────────────────

    /** Quotes an identifier (table name, column name) for this dialect. */
    String quoteIdentifier(String identifier);

    // ── Pagination ───────────────────────────────────────────────

    /** Returns the SQL fragment for LIMIT/OFFSET pagination. */
    String limitOffset(int limit, int offset);

    /** Returns the SQL fragment for LIMIT-only pagination. */
    String limit(int limit);

    // ── Table creation ───────────────────────────────────────────

    /** Returns the engine/storage clause appended after CREATE TABLE (e.g. ENGINE=InnoDB). */
    String tableEngine();

    /** Returns the SQL for "CREATE TABLE IF NOT EXISTS". */
    String createTableIfNotExists(String tableName);

    /** Returns the SQL for creating a migration tracking table. */
    String createMigrationsTable(String tableName);

    /** Returns the SQL for checking if a migration version exists. */
    String checkMigrationVersion(String tableName, int version);

    /** Returns the SQL for inserting a migration version record. */
    String insertMigrationVersion(String tableName, int version);

    // ── Foreign keys ─────────────────────────────────────────────

    /** Returns the ON DELETE CASCADE clause. */
    String onDeleteCascade();

    /** Returns the SQL for adding a foreign key constraint. */
    String foreignKeyConstraint(String constraintName, String column,
                                 String referencedTable, String referencedColumn);

    /** Returns the SQL for creating an index. */
    String createIndex(String indexName, String tableName, String column);

    // ── Dialect detection ────────────────────────────────────────

    /** Returns the JDBC driver class name for this dialect. */
    /**
     * JDBC driver class name. Used for explicit loading if needed.
     * Most modern JDBC 4.0+ drivers auto-register via SPI, so this may return empty.
     */
    String driverClass();

    /** Returns the default JDBC URL prefix for this dialect. */
    String urlPrefix();

    /** Returns the dialect name (e.g. "mysql", "postgresql", "sqlite"). */
    String name();

    /**
     * Detect the dialect from a JDBC URL.
     */
    static Dialect fromUrl(String jdbcUrl) {
        if (jdbcUrl == null) {
            throw new IllegalArgumentException("JDBC URL is null");
        }
        String lower = jdbcUrl.toLowerCase();
        if (lower.startsWith("jdbc:postgresql://") || lower.startsWith("jdbc:postgres://")) {
            return new PostgreSQLDialect();
        }
        if (lower.startsWith("jdbc:sqlite:")) {
            return new SQLiteDialect();
        }
        if (lower.startsWith("jdbc:mysql://") || lower.startsWith("jdbc:mariadb://")) {
            return new MySQLDialect();
        }
        throw new IllegalArgumentException("Unsupported database URL: " + jdbcUrl);
    }

    /**
     * Detect the dialect from a config name.
     */
    static Dialect fromName(String name) {
        return switch (name.toLowerCase()) {
            case "mysql", "mariadb" -> new MySQLDialect();
            case "postgresql", "postgres" -> new PostgreSQLDialect();
            case "sqlite" -> new SQLiteDialect();
            default -> throw new IllegalArgumentException("Unsupported database: " + name);
        };
    }
}
