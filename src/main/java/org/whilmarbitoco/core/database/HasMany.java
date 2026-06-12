package org.whilmarbitoco.core.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a one-to-many relationship.
 * Place on a field in the parent entity that holds a list of children.
 *
 * Example:
 * <pre>
 * {@code @Table(name = "users")}
 * public class User {
 *     {@code @Primary} {@code @Column(name = "id")}
 *     private int id;
 *
 *     {@code @HasMany(foreignKey = "user_id", referencedTable = "todos", referencedColumn = "id")}
 *     private List<Todo> todos;
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HasMany {
    /** The foreign key column on the child table. */
    String foreignKey();

    /** The child table name. */
    String referencedTable();

    /** The primary key column on the child table that the foreign key references. */
    String referencedColumn() default "id";
}
