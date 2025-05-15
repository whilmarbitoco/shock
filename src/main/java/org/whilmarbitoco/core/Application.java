package org.whilmarbitoco.core;

import org.whilmarbitoco.core.http.Server;
import org.whilmarbitoco.core.utils.Config;


public class Application {

    private final RouteRegistry routes;
    private final MiddlewareRegistry middlewares;
    private final Server server;


    public Application(RouteRegistry routes, MiddlewareRegistry middlewares) {
        this.routes = routes;
        this.middlewares = middlewares;
        this.server = new Server(Config.serverPort(), routes.getRouter(), middlewares);
    }

    public void run() {
        server.useGlobal(middlewares.globalMiddlewares());



        server.start();
    }
}
