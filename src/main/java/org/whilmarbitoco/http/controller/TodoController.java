package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.database.model.Todo;
import org.whilmarbitoco.database.repository.TodoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoController extends Controller {
    private final TodoRepository TODO = new TodoRepository();

    public String viewTodo(Request req, Response res) {

        Map<String, Object> context = new HashMap<>();

        List<Todo> todos = TODO.findWhere("user_id", "=", auth().getId());
        if (!todos.isEmpty()) {
            context.put("todos", todos.stream().map(Todo::getTodo).toList());
        }

        return view().render("todo.html", context);
    }

    public String addTodo(Request req, Response res) {
        String todo = (String) req.getBody("todo");

        TODO.create(new Todo(todo, auth().getId()));
        return res.redirect("/todo").toString();
    }

    public String deleteTodo(Request req, Response res) {
        String todo = (String) req.getBody("todo");

        TODO.deleteWhere("todo = ?", todo);
        return res.redirect("/todo").toString();
    }
}
