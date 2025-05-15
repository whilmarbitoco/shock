package org.whilmarbitoco.core.context;

import org.whilmarbitoco.core.http.Response;

public class ResponseContext {

    private static final ThreadLocal<Response> responseThreadLocal = new ThreadLocal<>();

    public static void set(Response response) {
        responseThreadLocal.set(response);
    }

    public static Response get() {
        return responseThreadLocal.get();
    }

    public static void remove() {
        responseThreadLocal.remove();
    }
}
