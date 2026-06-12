package org.whilmarbitoco.core.database;

import java.util.Map;

/**
 * SQLite dialect.
 * SQLite has limited ALTER TABLE support, so FK constraints are added inline.
 */
public class SQLiteDialect implements Dialect {

    @Override public String name() { return "sqlite"; }
    @Override public String driverClass() { return "org.sqlite.JDBC"; }
    @Override public String urlPrefix() { return "jdbc:sqlite:"; }

    @Override public String autoIncrement() { return "AUTOINCREMENT"; }
    @Override public String integerPKType() { return "INTEGER"; }
    @Override public String longPKType() { return "INTEGER"; }

    @Override
    public String resolveType(Class<?> javaType) {
        return typeMap().getOrDefault(javaType, "TEXT");
    }

    @Override
    public Map<Class<?>, String> typeMap() {
        return Map.ofEntries(
                Map.entry(int.class, "INTEGER"),
                Map.entry(Integer.class, "INTEGER"),
                Map.entry(long.class, "INTEGER"),
                Map.entry(Long.class, "INTEGER"),
                Map.entry(float.class, "REAL"),
                Map.entry(Float.class, "REAL"),
                Map.entry(double.class, "REAL"),
                Map.entry(Double.class, "REAL"),
                Map.entry(boolean.class, "INTEGER"),
                Map.entry(Boolean.class, "INTEGER"),
                Map.entry(String.class, "TEXT"),
                Map.entry(java.sql.Date.class, "TEXT"),
                Map.entry(java.sql.Time.class, "TEXT"),
                Map.entry(java.sql.Timestamp.class, "TEXT")
        );
    }

    @Override public String quoteIdentifier(String id) { return "\"" + id + "\""; }

    @Override
    public String limitOffset(int limit, int offset) {
        return "LIMIT " + limit + " OFFSET " + offset;
    }

    @Override
    public String limit(int limit) {
        return "LIMIT " + limit;
    }

    @Override public String tableEngine() { return ""; }

    @Override
    public String createTableIfNotExists(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + quoteIdentifier(tableName);
    }

    @Override
    public String createMigrationsTable(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + quoteIdentifier(tableName) + " ("
                + quoteIdentifier("version") + " INTEGER PRIMARY KEY, "
                + quoteIdentifier("applied_at") + " TEXT DEFAULT (datetime('now'))"
                + ")";
    }

    @Override
    public String checkMigrationVersion(String tableName, int version) {
        return "SELECT 1 FROM " + quoteIdentifier(tableName)
                + " WHERE " + quoteIdentifier("version") + " = " + version;
    }

    @Override
    public String insertMigrationVersion(String tableName, int version) {
        return "INSERT OR IGNORE INTO " + quoteIdentifier(tableName)
                + " (" + quoteIdentifier("version") + ") VALUES (" + version + ")";
    }

    @Override public String onDeleteCascade() { return "ON DELETE CASCADE"; }

    @Override
    public String foreignKeyConstraint(String constraintName, String column,
                                        String referencedTable, String referencedColumn) {
        // SQLite doesn't support named constraints in ADD CONSTRAINT, use inline
        return "FOREIGN KEY (" + quoteIdentifier(column) + ")"
                + " REFERENCES " + quoteIdentifier(referencedTable)
                + " (" + quoteIdentifier(referencedColumn) + ")"
                + " " + onDeleteCascade();
    }

    @Override
    public String createIndex(String indexName, String tableName, String column) {
        return "CREATE INDEX IF NOT EXISTS " + quoteIdentifier(indexName)
                + " ON " + quoteIdentifier(tableName)
                + " (" + quoteIdentifier(column) + ")";
    }
}
