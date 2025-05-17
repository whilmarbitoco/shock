package org.whilmarbitoco.core.database;

import java.util.List;

public record QueryResult<T>(List<T> list) {

    public T firstResult() {
        if (this.list == null || this.list.isEmpty()) return null;
        return this.list.getFirst();
    }

    public T lastResult() {
        if (this.list == null || this.list.isEmpty()) return null;
        return this.list.getLast();
    }
}