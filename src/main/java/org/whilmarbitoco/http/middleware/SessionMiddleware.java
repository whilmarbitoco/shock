package org.whilmarbitoco.http.middleware;

import org.whilmarbitoco.core.http.Middleware;
import org.whilmarbitoco.core.http.Request;
import org.whilmarbitoco.core.http.Response;
import org.whilmarbitoco.core.session.SessionManager;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.database.repository.UserRepository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SessionMiddleware implements Middleware {

    @Override
    public void handle(Request request, Response response) {
        String sessionId = Request.getCookie(request, "shock-session");

        String b = sessionId;
        if (sessionId == null|| !SessionManager.hasSession(sessionId)) {
            sessionId = UUID.randomUUID().toString();
            SessionManager.getSessions().put(sessionId, new HashMap<>());
            SessionManager.getFlash().put(sessionId, new HashMap<>());
            response.setCookie("shock-session", sessionId);
        }


        if (b != null && !b.equals(sessionId)) {
            System.out.println("[Session Before] " + b);
            System.out.println("[Session Changed] " + sessionId);
        }

        response.setShockSession(sessionId);
        request.setShockSession(sessionId);
    }

}
