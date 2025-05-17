package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.http.middleware.AuthMiddleware;
import org.whilmarbitoco.http.middleware.LogsMiddleware;
import org.whilmarbitoco.http.middleware.SessionMiddleware;

public class Middlewares extends MiddlewareRegistry {

    @Override
    public void global() {
        addGlobal(new LogsMiddleware()); // logs
        addGlobal(new SessionMiddleware()); // Global Session
    }

    @Override
    public void register() {
        add("auth", new AuthMiddleware());
    }
}
