package org.whilmarbitoco.controller;

import org.whilmarbitoco.core.http.MimeType;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public class UserController {

    public static void get(Request request, Response response) {

        System.out.println(response.toString());
        response.send("<h1> hello, <span style='color: red;'> World</span> </h1>");
    }
}
