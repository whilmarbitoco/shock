package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.utils.File;

public class View {

    protected String viewPath = Config.resources() + "view/";
    protected String template = viewPath + "template/" + Config.viewTemplate();
    protected String content = template;

    public View() {}

    public View(String view) {
        content = view;
    }

    public void template(String file) {
        template = viewPath + file;
    }

    @Override
    public String toString() {
        return assertContent();
    }

    private String assertContent() {
        if (content.endsWith(".html")) {
            return File.loadContent(viewPath + content);
        }
        return File.loadContent(template).replace("{{content}}", content);
    }
}
