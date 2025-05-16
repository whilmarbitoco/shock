package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.utils.File;

public class View {

    protected String viewPath = Config.viewPath();
    protected String template;

    public View() {}

    public View(String absolute) {
        this.template = absolute;
    }

    public void template(String template) {
        this.template = viewPath + template;
    }

    public String render(String view) {
        if (view == null) return "";

        if (view.endsWith(".html") && template != null) {
            String temp = File.loadContent(template);
            String content = Config.viewPath() + view;

            return temp.replace("{{content}}", content);
        }

        if (!view.endsWith(".html") && template != null) {
            String temp = File.loadContent(template);

            return temp.replace("{{content}}", view);
        }

        return view;
    }


}
