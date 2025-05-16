package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.http.controller.IndexController;
import org.whilmarbitoco.http.controller.TodoController;

public class Routes extends RouteRegistry {

    @Override
    public void register() {

        router.get("/todo", TodoController.class, "viewTodo");

    }
}