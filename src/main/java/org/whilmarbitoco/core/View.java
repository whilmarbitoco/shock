package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.utils.File;

public class View {

    protected static String viewPath = System.getProperty("user.dir") + "/src/main/resources/view/";
    protected static String template = viewPath + "template/" + Config.viewTemplate();

    public String render(String view) {

        String tmp = File.loadContent(template);

        return tmp.replace("{{content}}", view);
    }
}
