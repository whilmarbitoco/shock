package org.whilmarbitoco.core.registry;

import org.whilmarbitoco.core.router.Router;

public abstract class RouteRegistry {

    protected Router router;

    public RouteRegistry() {
        // No-op; router must be set via setRouter() before register() is called
    }

    public void setRouter(Router router) {
        this.router = router;
        register();
    }

    public abstract void register();

    public Router getRouter() {
        return router;
    }
}
