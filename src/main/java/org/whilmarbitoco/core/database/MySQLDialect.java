package org.whilmarbitoco.core.database;

import java.util.Map;

/**
 * MySQL / MariaDB dialect.
 */
public class MySQLDialect implements Dialect {

    @Override public String name() { return "mysql"; }
    @Override public String driverClass() { return "com.mysql.cj.jdbc.Driver"; }
    @Override public String urlPrefix() { return "jdbc:mysql://"; }

    @Override public String autoIncrement() { return "AUTO_INCREMENT"; }
    @Override public String integerPKType() { return "INT"; }
    @Override public String longPKType() { return "BIGINT"; }

    @Override
    public String resolveType(Class<?> javaType) {
        return typeMap().getOrDefault(javaType, "VARCHAR(255)");
    }

    @Override
    public Map<Class<?>, String> typeMap() {
        return Map.ofEntries(
                Map.entry(int.class, "INT"),
                Map.entry(Integer.class, "INT"),
                Map.entry(long.class, "BIGINT"),
                Map.entry(Long.class, "BIGINT"),
                Map.entry(float.class, "FLOAT"),
                Map.entry(Float.class, "FLOAT"),
                Map.entry(double.class, "DOUBLE"),
                Map.entry(Double.class, "DOUBLE"),
                Map.entry(boolean.class, "TINYINT(1)"),
                Map.entry(Boolean.class, "TINYINT(1)"),
                Map.entry(String.class, "VARCHAR(255)"),
                Map.entry(java.sql.Date.class, "DATE"),
                Map.entry(java.sql.Time.class, "TIME"),
                Map.entry(java.sql.Timestamp.class, "TIMESTAMP")
        );
    }

    @Override public String quoteIdentifier(String id) { return "`" + id + "`"; }

    @Override
    public String limitOffset(int limit, int offset) {
        return "LIMIT " + offset + ", " + limit;
    }

    @Override
    public String limit(int limit) {
        return "LIMIT " + limit;
    }

    @Override public String tableEngine() { return "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"; }

    @Override
    public String createTableIfNotExists(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + quoteIdentifier(tableName);
    }

    @Override
    public String createMigrationsTable(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + quoteIdentifier(tableName) + " ("
                + quoteIdentifier("version") + " INT PRIMARY KEY, "
                + quoteIdentifier("applied_at") + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ") ENGINE=InnoDB";
    }

    @Override
    public String checkMigrationVersion(String tableName, int version) {
        return "SELECT 1 FROM " + quoteIdentifier(tableName)
                + " WHERE " + quoteIdentifier("version") + " = " + version;
    }

    @Override
    public String insertMigrationVersion(String tableName, int version) {
        return "INSERT INTO " + quoteIdentifier(tableName)
                + " (" + quoteIdentifier("version") + ") VALUES (" + version + ")";
    }

    @Override public String onDeleteCascade() { return "ON DELETE CASCADE"; }

    @Override
    public String foreignKeyConstraint(String constraintName, String column,
                                        String referencedTable, String referencedColumn) {
        return "CONSTRAINT " + quoteIdentifier(constraintName)
                + " FOREIGN KEY (" + quoteIdentifier(column) + ")"
                + " REFERENCES " + quoteIdentifier(referencedTable)
                + " (" + quoteIdentifier(referencedColumn) + ")"
                + " " + onDeleteCascade();
    }

    @Override
    public String createIndex(String indexName, String tableName, String column) {
        return "INDEX " + quoteIdentifier(indexName)
                + " (" + quoteIdentifier(column) + ")";
    }
}
