package org.whilmarbitoco;

import org.whilmarbitoco.core.Application;
import org.whilmarbitoco.core.database.MigrationGenerator;
import org.whilmarbitoco.core.database.MigrationRunner;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.registry.Middlewares;
import org.whilmarbitoco.registry.Routes;

import java.io.IOException;


class Main {
    public static void main(String[] args) {
        RouteRegistry routes = new Routes();
        MiddlewareRegistry middlewares = new Middlewares();

        Application app = new Application(routes, middlewares);
//        runSideEffects();
        app.run();
    }

    private static void runSideEffects() {
        try {
            MigrationGenerator.generate("org.whilmarbitoco.database.model", "src/main/resources/migrations");
            MigrationRunner.run();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("Side Effects Done!");
        }


    }
}