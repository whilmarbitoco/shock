package org.whilmarbitoco.core;

import org.whilmarbitoco.core.session.SessionManager;
import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.view.View;
import org.whilmarbitoco.database.model.User;
import org.whilmarbitoco.http.middleware.AuthMiddleware;

import java.util.Map;

public class Controller {

    protected View view() {
        String path = Config.viewPath() + "template/" + Config.defaultViewTemplate();
        return new View(path);
    }

    protected View view(String template) {
        String path = Config.viewPath() + "template/" + template;
        return new View(path);
    }

    protected Map<String, String> flash(String session) {
        return SessionManager.flash(session);
    }

    protected boolean hasFlash(String session) {
        return SessionManager.hashFlash(session);
    }

    protected void clearFlash(String session) {
        flash(session).clear();
    }

    protected void setAuth(User auth) {
        AuthMiddleware.auth = auth;
    }

    protected User auth() {
        return AuthMiddleware.auth;
    }

}
