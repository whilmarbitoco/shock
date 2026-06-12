package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;

public class DemoController extends Controller {

    public String index(Request req, Response res) {
        // Use no-arg View so demo.html renders standalone (no template wrapper)
        return new org.whilmarbitoco.core.view.View().render("demo.html");
    }
}
