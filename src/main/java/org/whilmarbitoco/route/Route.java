package org.whilmarbitoco.route;

import org.whilmarbitoco.controller.UserController;
import org.whilmarbitoco.core.RouteRegistry;
import org.whilmarbitoco.core.http.MimeType;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public class Route extends RouteRegistry {

    @Override
    public void register() {
        router.get("/user", UserController::get);
    }
}
