package org.whilmarbitoco.http.middleware;

import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.database.model.Token;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.database.repository.TokenRepository;
import org.whilmarbitoco.database.repository.UserRepository;

import java.util.Optional;

public class AuthMiddleware implements Middleware {

    private final TokenRepository tokenRepository = new TokenRepository();
    private final UserRepository userRepository = new UserRepository();

    @Override
    public void handle(Request request, Response response) {
        String token = Request.getCookie(request, "user-auth");

        if (token == null) {
            response.redirect("/login");
            return;
        }

        Optional<Token> tok = tokenRepository.findWhere("token", "=", token).stream().findFirst();

        if (tok.isEmpty()) {
            response.redirect("/login");
            return;
        }

        Optional<User> user = userRepository.findWhere("id", "=", tok.get().getUserID()).stream().findFirst();
        user.ifPresent(request::setAuth);
    }
}
