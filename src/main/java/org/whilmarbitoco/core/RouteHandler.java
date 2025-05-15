package org.whilmarbitoco.core;

import org.whilmarbitoco.core.http.HttpRunnable;

import java.util.List;

public class RouteHandler {

    private HttpRunnable func;
    private String[] middlewares;

    public RouteHandler(HttpRunnable func, String[] middlewares) {
        this.func = func;
        this.middlewares = middlewares;
    }


    public HttpRunnable getFunc() {
        return func;
    }

    public String[] getMiddlewares() {
        return middlewares;
    }
}


