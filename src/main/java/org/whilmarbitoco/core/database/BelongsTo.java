package org.whilmarbitoco.core.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a many-to-one (belongs-to) relationship.
 * Place on a field in the child entity that references the parent.
 *
 * Example:
 * <pre>
 * {@code @Table(name = "todos")}
 * public class Todo {
 *     {@code @Primary} {@code @Column(name = "id")}
 *     private int id;
 *
 *     {@code @Column(name = "user_id")}
 *     private int userId;
 *
 *     {@code @BelongsTo(foreignKey = "user_id", referencedTable = "users", referencedColumn = "id")}
 *     private User user;
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BelongsTo {
    /** The foreign key column on this entity's table. */
    String foreignKey();

    /** The parent table name. */
    String referencedTable();

    /** The primary key column on the parent table. */
    String referencedColumn() default "id";
}
