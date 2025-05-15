package org.whilmarbitoco.core;

import org.whilmarbitoco.core.http.Server;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.exception.GlobalException;
import org.whilmarbitoco.registry.Exception;


public class Application {

    private final RouteRegistry routes;
    private final MiddlewareRegistry middlewares;
    private final Server server;
    private final GlobalException globalException;


    public Application(RouteRegistry routes, MiddlewareRegistry middlewares) {
        this.routes = routes;
        this.middlewares = middlewares;
        this.server = new Server(Config.serverPort(), routes.getRouter(), middlewares);
        this.globalException = new Exception();
    }

    public void run() {
        globalException.exceptionHandler();
        server.useGlobal(middlewares.globalMiddlewares());

        server.start();
    }
}
