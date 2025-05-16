package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController extends Controller {

    public static String get(Request request, Response response) {

        return view().render("form.html");
    }

    public static String create(Request request, Response response) {

        Map<String, Object> value = new HashMap<>();


        value.put("fruits", List.of("Apple", "Banana", "Cherry"));
        value.put("name", "john doe");
        value.put("email", "johndoe@gmail.com");
        value.put("year", LocalDate.now().getYear());

        return view().render("user.html", value);
    }
}
