package org.whilmarbitoco.core.view;

import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.utils.File;
import org.whilmarbitoco.core.view.node.Node;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View {

    public static final String TEMPLATE_EXTENSION = ".html";
    public static final String VIEW_EXTENSION = ".html";
    private final String viewPath = Config.viewPath();
    private String template = viewPath;

    public View() {}

    public View(String absolute) {
        this.template = absolute;
    }

    public void template(String template) {
        this.template = viewPath + template;
    }

    public String render(String view) {
        if (view == null) return "";

        if (view.endsWith(VIEW_EXTENSION) && !template.endsWith(TEMPLATE_EXTENSION)) {
            return File.loadContent(viewPath + view);
        }

        if (view.endsWith(VIEW_EXTENSION) && template.endsWith(TEMPLATE_EXTENSION)) {
            String temp = File.loadContent(template);
            String content = File.loadContent(Config.viewPath() + view);
            return temp.replace("{{content}}", content);
        }

        if (!view.endsWith(VIEW_EXTENSION) && template.endsWith(TEMPLATE_EXTENSION)) {
            String temp = File.loadContent(template);
            return temp.replace("{{content}}", view);
        }

        return view;
    }

    public String render(String view, Map<String, Object> context) {
        String template = render(view);
        Lexer lexer = new Lexer(template);
        Parser parser = new Parser(lexer);
        List<Node> nodes = parser.parse();

        StringBuilder body = new StringBuilder();
        for (Node node : nodes) {
            body.append(node.render(context));
        }
        return body.toString();
    }
}
