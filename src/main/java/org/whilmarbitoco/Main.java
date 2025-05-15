package org.whilmarbitoco;

import org.whilmarbitoco.core.Application;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.registry.Middlewares;
import org.whilmarbitoco.registry.Routes;


class Main {
    public static void main(String[] args) {
        RouteRegistry routes = new Routes();
        MiddlewareRegistry middlewares = new Middlewares();

        Application app = new Application(routes, middlewares);

        app.run();
    }
}