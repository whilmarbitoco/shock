package org.whilmarbitoco.core.router;

import org.whilmarbitoco.core.ControllerInvoker;
import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.http.HttpRunnable;
import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.registry.MiddlewareRegistry;
import org.whilmarbitoco.exception.InternalServerException;
import org.whilmarbitoco.http.controller.TodoController;
import org.whilmarbitoco.registry.Middlewares;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private void registerRoute(String method, String path, Class<?> func, String methodName, String... middlewares) {
        routes.get(method).putIfAbsent(path, new RouteHandler(func, methodName, middlewares));
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

        RouteHandler handler = route.get(path);

        for (String m : handler.getMiddlewares()) {
            middlewareRegistry.getMiddleware(m).handle(request, response);
        }

        if (!response.isHandled()) {
            Class<?> controller = handler.getController();
            String methodName = handler.getMethodName();
            String view = (String) ControllerInvoker.invoke(controller, methodName, request, response);
            response.send(view);
        }
    }
}











