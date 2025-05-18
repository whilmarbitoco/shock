package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.view.View;

public class IndexController extends Controller {

    public String get(Request request, Response response) {
        return view(null).render("index.html");
    }
}
