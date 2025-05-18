package org.whilmarbitoco.http.middleware;

import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.database.model.Token;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.database.repository.TokenRepository;
import org.whilmarbitoco.database.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class AuthMiddleware implements Middleware {
    public static User auth;

    private final TokenRepository TOKEN = new TokenRepository();
    private final UserRepository USER = new UserRepository();

    @Override
    public void handle(Request request, Response response) {
        String token = Request.getCookie(request, "user-auth");

        if (token == null) response.redirect("/login");

        Optional<Token> tok = TOKEN.findWhere("token", "=", token).stream().findFirst();

        if (tok.isEmpty()) response.redirect("/login");

        tok.ifPresent(t -> {
            if (AuthMiddleware.auth == null) {
                Optional<User> user = USER.findWhere("id", "=", t.getUserID())
                        .stream().findFirst();
                user.ifPresent(e -> AuthMiddleware.auth = e);
            }
        });
    }
}
