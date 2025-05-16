package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoController extends Controller {
    static List<String> todos = new ArrayList<>();

    public String viewTodo(Request req, Response res) {

        Map<String, Object> context = new HashMap<>();

        context.put("todos", todos);
        return view().render("todo.html", context);
    }

    public String addTodo(Request req, Response res) {
        String todo = (String) req.getBody("todo");

        todos.add(todo);
        res.redirect("/todo");
        return "";
    }
}
