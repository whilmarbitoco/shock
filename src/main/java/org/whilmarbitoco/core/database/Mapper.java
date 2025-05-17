package org.whilmarbitoco.core.database;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Mapper<T> {

    private final Class<T> type;

    public Mapper(Class<T> type) {
        this.type = type;

    }

    public T toEntity(ResultSet rs) {
        try {
            T obj = type.getDeclaredConstructor().newInstance();

            for (Field field : type.getDeclaredFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column == null) continue;

                field.setAccessible(true);
                Object value = rs.getObject(column.name());
                field.set(obj, value);
            }

            return obj;
        } catch (Exception e) {
            throw new RuntimeException("[Mapper] " + e);
        }
    }

    public PreparedStatement fromEntity(T entity, PreparedStatement stmt) {
        try {
            Field[] fields = entity.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                Primary primary = fields[i].getAnnotation(Primary.class);
                if (primary != null) continue;

                Column column = fields[i].getAnnotation(Column.class);
                if (column != null) {
                    fields[i].setAccessible(true);
                    stmt.setObject(i, fields[i].get(entity));
                }
            }

            return stmt;
        } catch (Exception e) {
            throw new RuntimeException("[Mapper] " + e);
        }
    }



}