package org.whilmarbitoco.core;

public abstract class RouteRegistry {

    protected final Router router = new Router();

    public abstract void register();

    public Router getRouter() {
        register();
        return router;
    }
}
