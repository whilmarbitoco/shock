package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.View;
import org.whilmarbitoco.core.exception.HttpException;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.utils.File;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UserController extends Controller {

    public static String get(Request request, Response response) {

        return view().render("form.html");
    }

    public static String create(Request request, Response response) {
        String name = (String) request.getBody("name");
        String email = (String) request.getBody("email");

        Map<String, Object> value = new HashMap<>();
        value.put("name", name);
        value.put("email", email);

        return view().render("<h1>Hello, {{name}}</h1><p>{{email}}</p>", value);
    }
}
