package org.whilmarbitoco;

import org.whilmarbitoco.core.Application;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.registry.Middleware;
import org.whilmarbitoco.registry.Route;


class Main {
    public static void main(String[] args) {
        RouteRegistry routes = new Route();
        MiddlewareRegistry middlewares = new Middleware();

        Application app = new Application(routes, middlewares);

        app.run();
    }
}