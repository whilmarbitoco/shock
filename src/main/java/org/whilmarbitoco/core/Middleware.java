package org.whilmarbitoco.core;

import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public interface Middleware {

    void handle(Request request, Response response);
}
