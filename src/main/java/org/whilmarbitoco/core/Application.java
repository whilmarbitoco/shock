package org.whilmarbitoco.core;

import org.whilmarbitoco.core.http.Server;
import org.whilmarbitoco.route.Route;

import java.time.LocalDate;

public class Application {

    private final RouteRegistry registry;
    private final Server server;


    public Application(RouteRegistry registry) {
        this.registry = new Route();
        this.server = new Server(Config.serverPort(), registry.getRouter());
    }

    public void run() {
//        server.setRouter(registry.getRouter());
        server.use(((request, response) -> {
            System.out.print(LocalDate.now() + " :: ");
            System.out.println(request.getMethod() + " " + request.getPath());
        }));



        server.start();
    }
}
