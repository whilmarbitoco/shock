package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.view.View;

public class Controller {

    protected static View view() {
        String path = Config.viewPath() + "template/" + Config.defaultViewTemplate();
        return new View(path);
    }

    protected static View view(String template) {
        String path = Config.viewPath() + "template/" + template;
        return new View(path);
    }

}
