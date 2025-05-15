package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.Config;

public class Controller {

    protected static View view = new View();
    protected static String viewPath = System.getProperty("user.dir") + "/src/main/resources/view/template/";
    protected static String template = viewPath + Config.viewTemplate();

}
