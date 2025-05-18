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

    protected String viewPath = Config.viewPath();
    protected String template = viewPath;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(.*?)}}");

    public View() {}

    public View(String absolute) {
        this.template = absolute;
    }

    public void template(String template) {
        this.template = viewPath + template;
    }

    public String render(String view) {
        if (view == null) return "";

        if (view.endsWith(".html") && !template.endsWith(".html")) {
            return File.loadContent(viewPath + view);
        }

        if (view.endsWith(".html") && template.endsWith(".html")) {
            String temp = File.loadContent(template);
            String content = File.loadContent(Config.viewPath() + view);

            return temp.replace("{{content}}", content);
        }

        if (!view.endsWith(".html") && template.endsWith(".html")) {
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

        StringBuilder output = new StringBuilder();
        for (Node node : nodes) {
            output.append(node.render(context));
        }

        return output.toString();
    }
}
