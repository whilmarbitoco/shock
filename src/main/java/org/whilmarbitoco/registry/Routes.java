package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.http.controller.IndexController;
import org.whilmarbitoco.http.controller.TodoController;

public class Routes extends RouteRegistry {

    @Override
    public void register() {
        router.get("/", IndexController::get);

        router.get("/todo", TodoController::viewTodo);
        router.post("/todo", TodoController::addTodo);
    }
}