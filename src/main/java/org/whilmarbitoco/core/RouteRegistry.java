package org.whilmarbitoco.core;

public abstract class RouteRegistry {

    protected final Router router = new Router();

    public RouteRegistry() {
        register();
    }

    public abstract void register();

    public Router getRouter() {
        return router;
    }
}
