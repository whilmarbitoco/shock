package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.http.controller.IndexController;
import org.whilmarbitoco.http.controller.TodoController;
import org.whilmarbitoco.http.controller.UserController;

public class Routes extends RouteRegistry {

    @Override
    public void register() {

        router.get("/", IndexController.class, "get");

//        Without Middleware
        router.get("/login", UserController.class, "loginGet");
        router.post("/login", UserController.class, "loginPost");


//        With Middleware
        router.get("/todo", TodoController.class, "viewTodo", "auth");
        router.post("/todo", TodoController.class, "addTodo", "auth");
        router.post("/todo/delete", TodoController.class, "deleteTodo", "auth");

    }
}