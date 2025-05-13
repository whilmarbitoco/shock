package org.whilmarbitoco;

import org.whilmarbitoco.core.Application;
import org.whilmarbitoco.core.RouteRegistry;
import org.whilmarbitoco.route.Route;

class Main {
    public static void main(String[] args) {
        RouteRegistry routes = new Route();
        Application app = new Application(routes);

        app.run();
    }
}