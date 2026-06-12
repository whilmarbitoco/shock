package org.whilmarbitoco.core.database;

import java.util.List;

public class Builder {

    private final StringBuilder query = new StringBuilder();

    public Builder insert(String table, List<String> cols) {
        query.setLength(0);
        query.append("INSERT INTO ").append(table).append(" (");
        query.append(String.join(", ", cols));
        query.append(") VALUES (");
        query.append("?, ".repeat(cols.size() - 1)).append("?");
        query.append(")");
        return this;
    }

    public Builder max(String primary, String tableName) {
        query.setLength(0);
        query.append("SELECT MAX(").append(primary).append(") as count ");
        query.append("FROM ").append(tableName);
        return this;
    }

    public Builder count(String tableName) {
        query.setLength(0);
        query.append("SELECT COUNT(*) AS `count` FROM ").append(tableName);
        return this;
    }

    public Builder raw(String condition) {
        query.append(condition);
        return this;
    }

    public Builder select(String tableName) {
        query.setLength(0);
        query.append("SELECT * FROM ").append(tableName);
        return this;
    }

    public Builder select(String tableName, List<String> columns) {
        query.setLength(0);
        query.append("SELECT ");
        query.append(String.join(", ", columns));
        query.append(" FROM ").append(tableName);
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
        query.append(" ORDER BY ").append(column);
        return this;
    }

    public Builder orderBy(String column, String direction) {
        query.append(" ORDER BY ").append(column).append(" ").append(direction);
        return this;
    }

    public Builder limit(int limit) {
        query.append(" LIMIT ").append(limit);
        return this;
    }

    public Builder offset(int offset) {
        query.append(" OFFSET ").append(offset);
        return this;
    }

    public Builder join(String joinClause) {
        query.append(" ").append(joinClause);
        return this;
    }

    public Builder innerJoin(String table, String onCondition) {
        query.append(" INNER JOIN ").append(table).append(" ON ").append(onCondition);
        return this;
    }

    public Builder leftJoin(String table, String onCondition) {
        query.append(" LEFT JOIN ").append(table).append(" ON ").append(onCondition);
        return this;
    }

    public Builder delete(String tableName) {
        query.setLength(0);
        query.append("DELETE FROM ");
        query.append(tableName);
        return this;
    }

    public Builder update(String tableName, List<String> columns) {
        query.setLength(0);
        query.append("UPDATE ");
        query.append(tableName);
        query.append(" SET ");
        query.append(String.join(" = ?, ", columns)).append(" = ?");
        return this;
    }

    public String build() {
        query.append(";");
        return query.toString();
    }
}
