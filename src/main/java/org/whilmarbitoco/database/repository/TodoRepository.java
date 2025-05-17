package org.whilmarbitoco.database.repository;

import org.whilmarbitoco.core.database.Repository;
import org.whilmarbitoco.database.model.Todo;

public class TodoRepository extends Repository<Todo> {
    public TodoRepository() {
        super(Todo.class);
    }
}
