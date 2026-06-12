package org.whilmarbitoco.core.database;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Mapper<T> {

    private final Class<T> type;
    private final List<Field> mappedFields;
    private final List<String> columnNames;

    public Mapper(Class<T> type) {
        this.type = type;
        this.mappedFields = new ArrayList<>();
        this.columnNames = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) continue;
            field.setAccessible(true);
            mappedFields.add(field);
            columnNames.add(column.name());
        }
    }

    public T toEntity(ResultSet rs) {
        try {
            T obj = type.getDeclaredConstructor().newInstance();
            for (int i = 0; i < mappedFields.size(); i++) {
                Object value = rs.getObject(columnNames.get(i));
                mappedFields.get(i).set(obj, value);
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("[Mapper] " + e);
        }
    }

    public PreparedStatement fromEntity(T entity, PreparedStatement stmt) {
        try {
            for (int i = 0; i < mappedFields.size(); i++) {
                Object value = mappedFields.get(i).get(entity);
                stmt.setObject(i + 1, value);
            }
            return stmt;
        } catch (Exception e) {
            throw new RuntimeException("[Mapper] " + e);
        }
    }
}
