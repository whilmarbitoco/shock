package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.database.model.Todo;
import org.whilmarbitoco.database.repository.TodoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoController extends Controller {
    private final TodoRepository todoRepo = new TodoRepository();

    public String viewTodo(Request req, Response res) {
        // Eager-load the user relationship on each todo
        List<Todo> todos = todoRepo.with("user").findWhere("user_id", "=", 1);

        Map<String, Object> context = new HashMap<>();
        context.put("todos", todos != null ? todos : List.of());

        return view().render("todo.html", context);
    }

    public String addTodo(Request req, Response res) {
        String todo = (String) req.getBody("todo");
        todoRepo.create(new Todo(todo, 1));
        res.redirect("/todo", 302);
        return view().render("rendered");
    }

    public String deleteTodo(Request req, Response res) {
        String todo = (String) req.getBody("todo");
        todoRepo.deleteWhere("todo = ?", todo);
        return res.redirect("/todo", 302).toString();
    }
}
