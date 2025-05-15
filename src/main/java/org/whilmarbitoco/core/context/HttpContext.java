package org.whilmarbitoco.core.context;

import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public class HttpContext {

    private static Response response;
    private static Request request;

    public static Response getResponse() {
        return response;
    }

    public static void setResponse(Response response) {
        HttpContext.response = response;
    }

    public static Request getRequest() {
        return request;
    }

    public static void setRequest(Request request) {
        HttpContext.request = request;
    }
}
