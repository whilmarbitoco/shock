package org.whilmarbitoco.core.database;

import java.util.List;

/**
 * SQL query builder. Dialect-aware for identifier quoting and pagination.
 *
 * Usage:
 * <pre>
 *   Dialect dialect = Dialect.fromUrl(config.dbUrl());
 *   Builder builder = new Builder(dialect);
 *   String sql = builder.select("users").where("id = ?").limit(10).build();
 * </pre>
 */
public class Builder {

    private final Dialect dialect;
    private final StringBuilder query = new StringBuilder();

    /** Creates a Builder with dialect auto-detected from config. */
    public Builder() {
        this(Dialect.fromUrl(org.whilmarbitoco.core.utils.Config.dbUrl()));
    }

    /** Creates a Builder with the given dialect. */
    public Builder(Dialect dialect) {
        this.dialect = dialect;
    }

    public Builder insert(String table, List<String> cols) {
        query.setLength(0);
        query.append("INSERT INTO ").append(dialect.quoteIdentifier(table)).append(" (");
        query.append(cols.stream().map(dialect::quoteIdentifier).reduce((a, b) -> a + ", " + b).orElse(""));
        query.append(") VALUES (");
        query.append("?, ".repeat(cols.size() - 1)).append("?");
        query.append(")");
        return this;
    }

    public Builder max(String primary, String tableName) {
        query.setLength(0);
        query.append("SELECT MAX(").append(dialect.quoteIdentifier(primary)).append(") as count ");
        query.append("FROM ").append(dialect.quoteIdentifier(tableName));
        return this;
    }

    public Builder count(String tableName) {
        query.setLength(0);
        query.append("SELECT COUNT(*) AS ").append(dialect.quoteIdentifier("count"))
                .append(" FROM ").append(dialect.quoteIdentifier(tableName));
        return this;
    }

    public Builder raw(String condition) {
        query.append(condition);
        return this;
    }

    public Builder select(String tableName) {
        query.setLength(0);
        query.append("SELECT * FROM ").append(dialect.quoteIdentifier(tableName));
        return this;
    }

    public Builder select(String tableName, List<String> columns) {
        query.setLength(0);
        query.append("SELECT ");
        query.append(columns.stream().map(dialect::quoteIdentifier).reduce((a, b) -> a + ", " + b).orElse(""));
        query.append(" FROM ").append(dialect.quoteIdentifier(tableName));
        return this;
    }

    public Builder where(String condition) {
        query.append(" WHERE ");
        query.append(condition);
        return this;
    }

    public Builder and(String condition) {
        query.append(" AND ");
        query.append(condition);
        return this;
    }

    public Builder or(String condition) {
        query.append(" OR ");
        query.append(condition);
        return this;
    }

    public Builder like(String str) {
        query.append(" LIKE ");
        query.append(str);
        return this;
    }

    public Builder orderBy(String column) {
        query.append(" ORDER BY ").append(dialect.quoteIdentifier(column));
        return this;
    }

    public Builder orderBy(String column, String direction) {
        query.append(" ORDER BY ").append(dialect.quoteIdentifier(column)).append(" ").append(direction);
        return this;
    }

    public Builder limit(int limit) {
        query.append(" ").append(dialect.limit(limit));
        return this;
    }

    public Builder offset(int offset) {
        query.append(" OFFSET ").append(offset);
        return this;
    }

    public Builder limitOffset(int limit, int offset) {
        query.append(" ").append(dialect.limitOffset(limit, offset));
        return this;
    }

    public Builder join(String joinClause) {
        query.append(" ").append(joinClause);
        return this;
    }

    public Builder innerJoin(String table, String onCondition) {
        query.append(" INNER JOIN ").append(dialect.quoteIdentifier(table)).append(" ON ").append(onCondition);
        return this;
    }

    public Builder leftJoin(String table, String onCondition) {
        query.append(" LEFT JOIN ").append(dialect.quoteIdentifier(table)).append(" ON ").append(onCondition);
        return this;
    }

    public Builder delete(String tableName) {
        query.setLength(0);
        query.append("DELETE FROM ");
        query.append(dialect.quoteIdentifier(tableName));
        return this;
    }

    public Builder update(String tableName, List<String> columns) {
        query.setLength(0);
        query.append("UPDATE ");
        query.append(dialect.quoteIdentifier(tableName));
        query.append(" SET ");
        query.append(columns.stream().map(c -> dialect.quoteIdentifier(c) + " = ?").reduce((a, b) -> a + ", " + b).orElse(""));
        return this;
    }

    public String build() {
        query.append(";");
        return query.toString();
    }
}
