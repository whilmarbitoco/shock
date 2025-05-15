package org.whilmarbitoco.core.router;

import org.whilmarbitoco.core.View;
import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.http.HttpRunnable;
import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.exception.InternalServerException;
import org.whilmarbitoco.registry.Middlewares;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final MiddlewareRegistry middlewareRegistry = new Middlewares();
    private final Map<String, Map<String, RouteHandler>> routes = new HashMap<>();

    public Router() {
        routes.put("GET", new HashMap<>());
        routes.put("POST", new HashMap<>());
        routes.put("DELETE", new HashMap<>());
        routes.put("PUT", new HashMap<>());


    }

    public void get(String path, HttpRunnable func) {
        registerRoute("GET", path, func);

    }

    public void get(String path, HttpRunnable func, String... middlewareNames) {
        registerRoute("GET", path, func, middlewareNames);
    }

    public void post(String path, HttpRunnable func) {
        registerRoute("POST", path, func);
    }

    private void registerRoute(String method, String path, HttpRunnable func, String... middlewares) {
        routes.get(method).putIfAbsent(path, new RouteHandler(func, middlewares));
    }

    public void resolve(Request request, Response response) throws HttpException {
        String method = request.getMethod();
        String path = request.getPath();

        var route = routes.get(method.toUpperCase());
        if (route == null) {
            throw new InternalServerException("Method " + method + " empty route");
        }

        if (route.get(path) == null) {
            throw new InternalServerException(method + " " + path + " not registered");
        }

        for (Middleware m : middlewareRegistry.globalMiddlewares()) {
            m.handle(request, response);
        }

        View view = route.get(path).getFunc().handle(request, response);
        response.send(view.toString());

    }

}
