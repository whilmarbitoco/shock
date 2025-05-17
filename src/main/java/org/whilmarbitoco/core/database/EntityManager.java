package org.whilmarbitoco.core.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EntityManager<T> {

    private final Class<T> type;


    public EntityManager(Class<T> type) {
        this.type = type;
    }


    public final List<String> getColumns() {
        List<String> attributes = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            if (field.getAnnotation(Primary.class) != null) continue;

            Column col = field.getAnnotation(Column.class);
            if (col != null) attributes.add(col.name());
        }

        return attributes;
    }

    public final String getTable() {
        return type.getAnnotation(Table.class).name();
    }

    public Optional<String> getPrimaryKey() {
        return Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.getAnnotation(Primary.class) != null)
                .map(Field::getName).findFirst();


    }

    private Field findPrimaryField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Primary.class)) {
                return field;
            }
        }
        throw new RuntimeException("No @Primary field found in related entity: " + clazz.getName());
    }

    public Object getPrimaryKeyValue(T entity) {
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                Primary primary = field.getAnnotation(Primary.class);
                if (primary == null) continue;
                field.setAccessible(true);
                return field.get(entity);
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void validate(T entity) {
        try {
            Field[] fields = entity.getClass().getDeclaredFields();

            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                if (column == null) continue;
                field.setAccessible(true);
                if (field.get(entity) == null)
                    throw new RuntimeException("Field cannot be empty for " + column.name());
            }
        } catch (Exception err) {
            throw new RuntimeException("[Entity.java] Something went wrong. INFO:: " + err.getMessage());
        }
    }

    public void isValidColumn(String column) {
        try {
            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {
                Column col = field.getAnnotation(Column.class);
                if (col == null) continue;
                if (col.name().equals(column)) return;
            }
            throw new RuntimeException("Unknown column " + column);
        } catch (Exception err) {
            throw new RuntimeException("[Entity.java] Something went wrong. INFO:: " + err.getMessage());
        }
    }

    public void isValidCondition(String condition) {
        List<String> conditions = List.of("=", "!=", "<", ">", "<=", ">=");

        if (!conditions.contains(condition))
            throw new RuntimeException("[Repository] Invalid Condition:: " + condition);
    }

}