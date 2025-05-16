package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.http.middleware.AuthMiddleware;
import org.whilmarbitoco.http.middleware.LogsMiddleware;

public class Middlewares extends MiddlewareRegistry {

    @Override
    public void global() {
        addGlobal(new LogsMiddleware()); // logs
    }

    @Override
    public void register() {
        add("auth", new AuthMiddleware());
    }
}
