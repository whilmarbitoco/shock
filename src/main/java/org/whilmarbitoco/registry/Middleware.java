package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.MiddlewareRegistry;
import org.whilmarbitoco.http.middleware.LogsMiddleware;

public class Middleware extends MiddlewareRegistry {

    @Override
    public void global() {
        addGlobal(new LogsMiddleware()); // logs
    }

    @Override
    public void register() {
        add("log", new LogsMiddleware());
    }
}
