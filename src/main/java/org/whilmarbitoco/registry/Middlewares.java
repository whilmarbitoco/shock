package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.http.middleware.AuthMiddleware;
import org.whilmarbitoco.http.middleware.LogsMiddleware;
import org.whilmarbitoco.http.middleware.SessionMiddleware;

public class Middlewares extends MiddlewareRegistry {

//    Global Middlewares
    @Override
    public void global() {
        addGlobal(new LogsMiddleware()); // logs
        addGlobal(new SessionMiddleware()); // Global Session
    }

//    Custom Middleware
    @Override
    public void register() {
        add("auth", new AuthMiddleware());
    }
}
