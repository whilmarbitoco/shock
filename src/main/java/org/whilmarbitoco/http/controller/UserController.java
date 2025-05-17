package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.database.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserController extends Controller {

    private final UserRepository userRepository = new UserRepository();

    public String loginGet(Request req, Response res) {
        Map<String, Object> context = new HashMap<>();

        if (flash(res.getShockSession()).containsKey("error")) {
            context.put("error", flash(res.getShockSession()).get("error"));
        }

        return view().render("login.html", context);
    }

    public String loginPost(Request req, Response res) {
        String email = (String) req.getBody("email");
        String password = (String) req.getBody("password");

        Optional<User> user = userRepository.findWhere("email", "=", email).stream().findFirst();

        if (user.isEmpty()) {;
            flash(req.getShockSession()).put("error", "Email not registered");
            return res.redirect("/login", 302).toString();
        }

        if (!user.get().getPassword().equals(password)) {
            flash(req.getShockSession()).put("error", "Email or Password invalid");
            return res.redirect("/login", 302).toString();
        }

        return null;
    }

}
