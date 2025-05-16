package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.http.controller.IndexController;
import org.whilmarbitoco.http.controller.TodoController;

public class Routes extends RouteRegistry {

    @Override
    public void register() {

        router.get("/", IndexController.class, "get");

        router.get("/todo", TodoController.class, "viewTodo", "auth");
        router.post("/todo", TodoController.class, "addTodo", "auth");

    }
}