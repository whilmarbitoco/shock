package org.whilmarbitoco.core.router;

import org.whilmarbitoco.core.http.HttpRunnable;

public class RouteHandler {

    private Class<?> controller;
    private String methodName;
    private String[] middlewares;

    public RouteHandler(Class<?> controller, String methodName) {
        this.controller = controller;
        this.methodName = methodName;
    }

    public RouteHandler(Class<?> controller, String methodName,String[] middlewares) {
        this.controller = controller;
        this.methodName = methodName;
        this.middlewares = middlewares;
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


