package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.View;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.utils.File;

public class UserController extends Controller {

    public static View get(Request request, Response response) {
        String search = (String) request.getParam("search");

        return new View("index.html");
    }
}
