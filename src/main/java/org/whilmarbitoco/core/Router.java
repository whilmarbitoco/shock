package org.whilmarbitoco.core;

import org.whilmarbitoco.core.http.HttpRunnable;
import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {

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
        if (routes.get(method).containsKey(path)) {
            throw new RuntimeException(method + " /" + path + " already registered");
        }

        routes.get(method).put(path, new RouteHandler(func, middlewares));
    }

    public Map<String, Map<String, RouteHandler>> getRoutes() {
        return routes;
    }
}
