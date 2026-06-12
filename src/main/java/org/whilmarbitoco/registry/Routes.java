package org.whilmarbitoco.registry;

import org.whilmarbitoco.core.registry.RouteRegistry;
import org.whilmarbitoco.http.controller.DemoController;
import org.whilmarbitoco.http.controller.IndexController;
import org.whilmarbitoco.http.controller.TodoController;

public class Routes extends RouteRegistry {

    @Override
    public void register() {

        router.get("/", IndexController.class, "get");
        router.get("/demo", DemoController.class, "index");

//        Without Middleware
//        router.get("/login", UserController.class, "loginGet");
//        router.post("/login", UserController.class, "loginPost");


//        With Middleware
        router.get("/todo", TodoController.class, "viewTodo");
        router.post("/todo", TodoController.class, "addTodo");
        router.post("/todo/delete", TodoController.class, "deleteTodo");

    }
}