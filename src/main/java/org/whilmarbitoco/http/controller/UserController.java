package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.session.SessionManager;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.database.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserController extends Controller {

    private final UserRepository userRepository = new UserRepository();

    public String loginGet(Request req, Response res) {
        Map<String, Object> context = new HashMap<>();

        String err = SessionManager.flash(req.getSession()).get("error");

        System.out.println("error:: " + err);
        return view().render("login.html", context);
    }

    public String loginPost(Request req, Response res) {
        String email = (String) req.getBody("email");
        String password = (String) req.getBody("password");

        Optional<User> user = userRepository.findWhere("email", "=", email).stream().findFirst();

        if (user.isEmpty()) {
            SessionManager.flash(req.getSession()).put("error", "Email not registered");
            return res.redirect("/login", 302).toString();
        }

        if (!user.get().getPassword().equals(password)) {
            return res.redirect("/login", 302).toString();
        }

        return null;
    }

}
