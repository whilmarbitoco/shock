package org.whilmarbitoco.http.controller;

import org.whilmarbitoco.core.Controller;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.database.model.Token;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.database.repository.TokenRepository;
import org.whilmarbitoco.database.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserController extends Controller {

    private final UserRepository USER = new UserRepository();
    private final TokenRepository TOKEN = new TokenRepository();

//    Sample GET controller
    public String loginGet(Request req, Response res) {
        Map<String, Object> context = new HashMap<>();

        if (flash(res.getShockSession()).containsKey("error")) {
            context.put("error", flash(res.getShockSession()).get("error"));
            clearFlash(res.getShockSession());
        }

        return view().render("login.html", context);
    }

//    Sample POST controller
    public String loginPost(Request req, Response res) {
        String email = (String) req.getBody("email");
        String password = (String) req.getBody("password");

        Optional<User> user = USER.findWhere("email", "=", email)
                .stream().findFirst();

        if (user.isEmpty()) {
            flash(req.getShockSession()).put("error", "Email not registered");
            return res.redirect("/login", 301).toString();
        }

        if (!user.get().getPassword().equals(password)) {
            flash(req.getShockSession()).put("error", "Email or Password invalid");
            return res.redirect("/login", 301).toString();
        }

        String session = UUID.randomUUID().toString();
        setAuth(user.get());
        TOKEN.create(new Token(session, user.get().getId()));
        res.setCookie("user-auth", session);
        res.redirect("/todo", 302);
        return "";
    }

}
