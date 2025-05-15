package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.http.MimeType;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.utils.File;

public class UserController {

    public static void get(Request request, Response response) {

        String p = System.getProperty("user.dir") + "/src/main/resources/view/template/";
        String c = File.loadContent(p + "index.html");

        response.send(c);
    }
}
