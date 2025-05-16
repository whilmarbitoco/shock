package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.http.controller.UserController;

public class Routes extends RouteRegistry {

    @Override
    public void register() {
        router.get("/", UserController::get);

        router.get("/user", UserController::create);
    }
}