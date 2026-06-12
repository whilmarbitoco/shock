package org.whilmarbitoco.core.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Handles relationship loading for entities.
 *
 * Supports two modes:
 * 1. Eager loading — use {@link #eagerLoad(Object, String...)} to load relations
 *    on a single entity after fetching it.
 * 2. Batch eager loading — use {@link #eagerLoadAll(List, String...)} to load
 *    relations for a list of entities with one query per relation (N+1 safe).
 *
 * Usage:
 * <pre>
 *   User user = userRepo.findByID(1);
 *   relationManager.eagerLoad(user, "todos");
 *   // user.getTodos() now populated
 * </pre>
 */
public class RelationManager {

    private final Connection connection;

    public RelationManager(Connection connection) {
        this.connection = connection;
    }

    /**
     * Eager-load named relations on a single entity.
     */
    public void eagerLoad(Object entity, String... relationFields) {
        for (String fieldName : relationFields) {
            loadRelation(entity, fieldName);
        }
    }

    /**
     * Eager-load named relations on a list of entities (batch query per relation).
     */
    public void eagerLoadAll(List<?> entities, String... relationFields) {
        for (String fieldName : relationFields) {
            loadRelationBatch(entities, fieldName);
        }
    }

    private void loadRelation(Object entity, String fieldName) {
        Class<?> type = entity.getClass();
        try {
            Field field = findField(type, fieldName);
            if (field == null) return;

            HasMany hasMany = field.getAnnotation(HasMany.class);
            if (hasMany != null) {
                loadHasMany(entity, field, hasMany);
                return;
            }

            BelongsTo belongsTo = field.getAnnotation(BelongsTo.class);
            if (belongsTo != null) {
                loadBelongsTo(entity, field, belongsTo);
            }
        } catch (Exception e) {
            throw new RuntimeException("[RelationManager] Failed to load '" + fieldName + "' on " + type.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    private void loadRelationBatch(List<?> entities, String fieldName) {
        if (entities.isEmpty()) return;

        Class<?> type = entities.get(0).getClass();
        try {
            Field field = findField(type, fieldName);
            if (field == null) return;

            HasMany hasMany = field.getAnnotation(HasMany.class);
            if (hasMany != null) {
                loadHasManyBatch(entities, field, hasMany);
                return;
            }

            BelongsTo belongsTo = field.getAnnotation(BelongsTo.class);
            if (belongsTo != null) {
                loadBelongsToBatch(entities, field, belongsTo);
            }
        } catch (Exception e) {
            throw new RuntimeException("[RelationManager] Batch load failed for '" + fieldName + "' on " + type.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    // ── HasMany: one parent → many children ────────────────────────

    private void loadHasMany(Object parent, Field field, HasMany ann) throws Exception {
        Object pkValue = getPrimaryKeyValue(parent);
        if (pkValue == null) return;

        String sql = "SELECT * FROM " + ann.referencedTable()
                   + " WHERE " + ann.foreignKey() + " = ?";

        Mapper<?> childMapper = new Mapper<>(field.getType().getGenericSuperclass() == null
                ? Object.class : Object.class); // resolved via reflection below

        // Determine the child list type from the field
        Class<?> childType = getListElementType(field);
        if (childType == null) return;

        Mapper<?> mapper = new Mapper<>(childType);
        List<Object> children = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, pkValue);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    children.add(mapper.toEntity(rs));
                }
            }
        }

        field.setAccessible(true);
        field.set(parent, children);
    }

    private void loadHasManyBatch(List<?> parents, Field field, HasMany ann) throws Exception {
        Class<?> childType = getListElementType(field);
        if (childType == null) return;

        // Collect all parent IDs
        List<Object> ids = new ArrayList<>();
        for (Object parent : parents) {
            Object pk = getPrimaryKeyValue(parent);
            if (pk != null) ids.add(pk);
        }
        if (ids.isEmpty()) return;

        // Build IN clause
        String placeholders = String.join(", ", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT * FROM " + ann.referencedTable()
                   + " WHERE " + ann.foreignKey() + " IN (" + placeholders + ")";

        Mapper<?> mapper = new Mapper<>(childType);
        Map<Object, List<Object>> grouped = new LinkedHashMap<>();
        // Initialize empty lists keyed by parent PK
        for (Object id : ids) grouped.put(id, new ArrayList<>());

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                stmt.setObject(i + 1, ids.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object child = mapper.toEntity(rs);
                    Object fkVal = getFieldValue(child, ann.foreignKey());
                    if (fkVal != null) {
                        grouped.computeIfAbsent(fkVal, k -> new ArrayList<>()).add(child);
                    }
                }
            }
        }

        for (Object parent : parents) {
            Object pk = getPrimaryKeyValue(parent);
            field.setAccessible(true);
            field.set(parent, grouped.getOrDefault(pk, new ArrayList<>()));
        }
    }

    // ── BelongsTo: many children → one parent ──────────────────────

    private void loadBelongsTo(Object child, Field field, BelongsTo ann) throws Exception {
        Object fkValue = getFieldValue(child, ann.foreignKey());
        if (fkValue == null) return;

        String sql = "SELECT * FROM " + ann.referencedTable()
                   + " WHERE " + ann.referencedColumn() + " = ? LIMIT 1";

        Class<?> parentType = field.getType();
        Mapper<?> mapper = new Mapper<>(parentType);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, fkValue);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    field.setAccessible(true);
                    field.set(child, mapper.toEntity(rs));
                }
            }
        }
    }

    private void loadBelongsToBatch(List<?> children, Field field, BelongsTo ann) throws Exception {
        Class<?> parentType = field.getType();

        // Collect all FK values
        Set<Object> fkValues = new LinkedHashSet<>();
        for (Object child : children) {
            Object fk = getFieldValue(child, ann.foreignKey());
            if (fk != null) fkValues.add(fk);
        }
        if (fkValues.isEmpty()) return;

        // Fetch all referenced parents in one query
        String placeholders = String.join(", ", Collections.nCopies(fkValues.size(), "?"));
        String sql = "SELECT * FROM " + ann.referencedTable()
                   + " WHERE " + ann.referencedColumn() + " IN (" + placeholders + ")";

        Mapper<?> mapper = new Mapper<>(parentType);
        Map<Object, Object> parentsById = new LinkedHashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int idx = 1;
            for (Object v : fkValues) stmt.setObject(idx++, v);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object parent = mapper.toEntity(rs);
                    parentsById.put(getPrimaryKeyValue(parent), parent);
                }
            }
        }

        for (Object child : children) {
            Object fk = getFieldValue(child, ann.foreignKey());
            if (fk != null && parentsById.containsKey(fk)) {
                field.setAccessible(true);
                field.set(child, parentsById.get(fk));
            }
        }
    }

    // ── Helpers ────────────────────────────────────────────────────

    private Field findField(Class<?> type, String name) {
        while (type != null && type != Object.class) {
            try {
                return type.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            }
        }
        return null;
    }

    private Object getPrimaryKeyValue(Object entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Primary.class)) {
                field.setAccessible(true);
                try {
                    return field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    private Object getFieldValue(Object entity, String fieldName) {
        try {
            Field field = findField(entity.getClass(), fieldName);
            if (field == null) return null;
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Best-effort extraction of the generic type parameter from a List field.
     * Returns null if it cannot be determined at runtime (type erasure).
     */
    private Class<?> getListElementType(Field field) {
        java.lang.reflect.Type genericType = field.getGenericType();
        if (genericType instanceof java.lang.reflect.ParameterizedType pt) {
            java.lang.reflect.Type[] typeArgs = pt.getActualTypeArguments();
            if (typeArgs.length > 0 && typeArgs[0] instanceof Class<?> cls) {
                return cls;
            }
        }
        return null;
    }
}
