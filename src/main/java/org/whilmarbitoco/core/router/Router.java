package org.whilmarbitoco.core.router;

import org.whilmarbitoco.core.ControllerInvoker;
import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.exception.InternalServerException;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final MiddlewareRegistry middlewareRegistry;
    private final Map<String, Map<String, RouteHandler>> routes = new HashMap<>();

    public Router(MiddlewareRegistry middlewareRegistry) {
        this.middlewareRegistry = middlewareRegistry;
        routes.put("GET", new HashMap<>());
        routes.put("POST", new HashMap<>());
        routes.put("DELETE", new HashMap<>());
        routes.put("PUT", new HashMap<>());
        routes.put("PATCH", new HashMap<>());
    }

    public void get(String path, Class<?> func, String method) {
        registerRoute("GET", path, func, method);
    }

    public void get(String path, Class<?> func, String method, String... middleware) {
        registerRoute("GET", path, func, method, middleware);
    }

    public void post(String path, Class<?> func, String method) {
        registerRoute("POST", path, func, method);
    }

    public void post(String path, Class<?> func, String method, String... middleware) {
        registerRoute("POST", path, func, method, middleware);
    }

    public void put(String path, Class<?> func, String method) {
        registerRoute("PUT", path, func, method);
    }

    public void put(String path, Class<?> func, String method, String... middleware) {
        registerRoute("PUT", path, func, method, middleware);
    }

    public void patch(String path, Class<?> func, String method) {
        registerRoute("PATCH", path, func, method);
    }

    public void patch(String path, Class<?> func, String method, String... middleware) {
        registerRoute("PATCH", path, func, method, middleware);
    }

    public void delete(String path, Class<?> func, String method) {
        registerRoute("DELETE", path, func, method);
    }

    public void delete(String path, Class<?> func, String method, String... middleware) {
        registerRoute("DELETE", path, func, method, middleware);
    }

    private void registerRoute(String method, String path, Class<?> func, String methodName, String... middlewares) {
        routes.get(method).putIfAbsent(path, new RouteHandler(func, methodName, middlewares));
    }

    public void resolve(Request request, Response response) throws HttpException {
        String method = request.getMethod();
        String path = request.getPath();

        var route = routes.get(method.toUpperCase());
        if (route == null) {
            throw new InternalServerException("Method " + method + " not supported");
        }

        MatchResult result = matchRoute(route, path);
        if (result == null) {
            throw new InternalServerException(method + " " + path + " not registered");
        }

        for (var entry : result.params.entrySet()) {
            request.setRouteParam(entry.getKey(), entry.getValue());
        }

        for (Middleware m : middlewareRegistry.globalMiddlewares()) {
            m.handle(request, response);
        }

        for (String m : result.handler.getMiddlewares()) {
            middlewareRegistry.getMiddleware(m).handle(request, response);
        }

        if (!response.isHandled()) {
            Class<?> controller = result.handler.getController();
            String methodName = result.handler.getMethodName();
            String view = (String) ControllerInvoker.invoke(controller, methodName, request, response);
            response.send(view);
        }
    }

    private MatchResult matchRoute(Map<String, RouteHandler> methodRoutes, String path) {
        RouteHandler exact = methodRoutes.get(path);
        if (exact != null) return new MatchResult(exact, Map.of());

        for (var entry : methodRoutes.entrySet()) {
            String pattern = entry.getKey();
            if (!pattern.contains(":")) continue;

            String[] patternParts = pattern.split("/");
            String[] pathParts = path.split("/");
            if (patternParts.length != pathParts.length) continue;

            boolean matches = true;
            Map<String, String> params = new HashMap<>();
            for (int i = 0; i < patternParts.length; i++) {
                if (patternParts[i].startsWith(":")) {
                    params.put(patternParts[i].substring(1), pathParts[i]);
                } else if (!patternParts[i].equals(pathParts[i])) {
                    matches = false;
                    break;
                }
            }
            if (matches) return new MatchResult(entry.getValue(), params);
        }
        return null;
    }

    private static class MatchResult {
        final RouteHandler handler;
        final Map<String, String> params;

        MatchResult(RouteHandler handler, Map<String, String> params) {
            this.handler = handler;
            this.params = params;
        }
    }
}
