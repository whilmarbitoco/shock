package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.Config;

public class Controller {


    protected static View view() {
        String path = Config.viewPath() + "template/" + Config.defaultViewTemplate();
        return new View(path);
    }

}
