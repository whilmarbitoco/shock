package org.whilmarbitoco.http.middleware;

import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

import java.time.LocalDate;

public class LogsMiddleware implements Middleware {

    @Override
    public void handle(Request request, Response response) {
        System.out.print("[Middleware] ");
        System.out.print(LocalDate.now() + " :: ");
        System.out.println(request.getMethod() + " " + request.getPath());
    }
}
