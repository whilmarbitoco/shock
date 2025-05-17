package org.whilmarbitoco.core;

import org.whilmarbitoco.core.session.SessionManager;
import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.view.View;

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
}
