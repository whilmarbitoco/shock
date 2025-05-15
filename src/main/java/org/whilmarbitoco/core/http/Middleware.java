package org.whilmarbitoco.core.http;

public interface Middleware {

    void handle(Request request, Response response);
}
