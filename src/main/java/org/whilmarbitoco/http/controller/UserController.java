package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.View;
import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.utils.File;

import java.time.LocalDate;
import java.util.Map;

public class UserController extends Controller {

    public static String get(Request request, Response response) {
        String name = (String) request.getParam("name");

        String view = "<h1>Welcome, {{name}}</h1>";

        return view().render(view, Map.of("name", name == null ? "Shock" : name, "year", LocalDate.now().getYear()));
    }
}
