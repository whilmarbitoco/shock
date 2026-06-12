package org.whilmarbitoco.core.router;

public class RouteHandler {

    private final Class<?> controller;
    private final String methodName;
    private final String[] middlewares;

    public RouteHandler(Class<?> controller, String methodName) {
        this.controller = controller;
        this.methodName = methodName;
        this.middlewares = new String[0];
    }

    public RouteHandler(Class<?> controller, String methodName, String[] middlewares) {
        this.controller = controller;
        this.methodName = methodName;
        this.middlewares = middlewares != null ? middlewares : new String[0];
    }

    public Class<?> getController() {
        return controller;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getMiddlewares() {
        return middlewares;
    }
}
