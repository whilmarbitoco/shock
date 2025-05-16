package org.whilmarbitoco.http.middleware;

import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public class AuthMiddleware implements Middleware {

    @Override
    public void handle(Request request, Response response) {
        String token = request.getHeader("Authorization");

        if (token == null) response.redirect("/");
    }
}
