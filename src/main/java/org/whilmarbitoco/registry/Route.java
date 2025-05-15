package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.http.controller.UserController;

public class Route extends RouteRegistry {

    @Override
    public void register() {
        router.get("/", UserController::get);
    }
}