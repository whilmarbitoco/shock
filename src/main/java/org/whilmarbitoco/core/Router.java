package org.whilmarbitoco.core;

import org.whilmarbitoco.core.http.HttpRunnable;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<String, Map<String, HttpRunnable>> routes;

    public Router() {
        routes = new HashMap<>();
        routes.put("GET", new HashMap<>());
        routes.put("POST", new HashMap<>());
        routes.put("DELETE", new HashMap<>());
        routes.put("PUT", new HashMap<>());
    }

    public void get(String path, HttpRunnable func) {
        if (routes.get("GET").containsKey(path)) {
            throw new RuntimeException("GET /" + path + " already registered");
        }

        routes.get("GET").put(path, func);
    }

    public void post(String path, HttpRunnable func) {
        if (routes.get("GET").containsKey(path)) {
            throw new RuntimeException("POST /" + path + " already registered");
        }

        routes.get("POST").put(path, func);
    }

    public void assertRoute(String method, String path, Request req, Response res) {
        if (routes.get(method.toUpperCase()).isEmpty()) {
            System.out.println("Empty routes");
            return;
        }

        if (routes.get(method.toUpperCase()).containsKey(path)) {
            routes.get(method.toUpperCase()).get(path).handle(req, res);
        }
    }

}
